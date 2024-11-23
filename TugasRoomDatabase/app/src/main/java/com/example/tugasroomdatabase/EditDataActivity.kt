package com.example.tugasroomdatabase

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tugasroomdatabase.database.DataPemilih
import com.example.tugasroomdatabase.database.DataPemilihDao
import com.example.tugasroomdatabase.database.DataPemilihRoomDatabase
import com.example.tugasroomdatabase.databinding.ActivityEditDataBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class EditDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditDataBinding
    private lateinit var mDataPemilihDao: DataPemilihDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi ExecutorService
        executorService = Executors.newSingleThreadExecutor()

        val db = DataPemilihRoomDatabase.getDatabase(this)
        mDataPemilihDao = db!!.pemilihDao()!!

        // Ambil data dari intent
        val idPemilihRead = intent.getIntExtra("ID_PEMILIH", -1) // Default -1 jika ID tidak ditemukan
        val namaPemilihRead = intent.getStringExtra("NAMA_PEMILIH") ?: ""
        val nikPemilihRead = intent.getStringExtra("NIK_PEMILIH") ?: ""
        val alamatPemilihRead = intent.getStringExtra("ALAMAT_PEMILIH") ?: ""

        // Set nilai awal di field
        with(binding) {
            namaPemilih.setText(namaPemilihRead)
            nik.setText(nikPemilihRead)
            alamat.setText(alamatPemilihRead)
        }

        // Tombol Edit
        with(binding) {
            btnEdit.setOnClickListener {
                val gender = getSelectedGender() // Ambil gender dari RadioGroup
                if (gender != null && checkAllField(namaPemilih, nik, alamat)) {
                    update(
                        DataPemilih(
                            id = idPemilihRead,
                            nama_pemilih = namaPemilih.text.toString(),
                            nik = nik.text.toString().toIntOrNull() ?: 0,
                            gender = gender,
                            alamat = alamat.text.toString()
                        )
                    )
                    setEmptyField()
                    Toast.makeText(this@EditDataActivity, "Data berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                    finish() // Kembali ke activity sebelumnya
                } else {
                    Toast.makeText(this@EditDataActivity, "Pilih gender terlebih dahulu!", Toast.LENGTH_SHORT).show()
                }
            }

            // Tombol Back
            btnBack.setOnClickListener {
                startActivity(Intent(this@EditDataActivity, HomeActivity::class.java))
            }
        }
    }

    private fun update(dataPemilih: DataPemilih) {
        executorService.execute {
            mDataPemilihDao.update(dataPemilih)
        }
    }

    /**
     * Fungsi untuk mendapatkan gender dari RadioGroup
     */
    private fun getSelectedGender(): String? {
        val selectedId = binding.gender.checkedRadioButtonId
        return if (selectedId != -1) {
            val selectedRadioButton = findViewById<RadioButton>(selectedId)
            selectedRadioButton.text.toString()
        } else {
            null // Tidak ada gender yang dipilih
        }
    }

    /**
     * Fungsi untuk mengosongkan input field
     */
    private fun setEmptyField() {
        with(binding) {
            namaPemilih.setText("")
            nik.setText("")
            alamat.setText("")
            gender.clearCheck() // Reset RadioGroup
        }
    }

    /**
     * Fungsi untuk validasi semua field
     */
    private fun checkAllField(namaPemilih: EditText, nik: EditText, alamat: EditText): Boolean {
        return if (namaPemilih.text.isEmpty() || nik.text.isEmpty() || alamat.text.isEmpty()) {
            Toast.makeText(this, "Harap isi semua data!", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }
}
