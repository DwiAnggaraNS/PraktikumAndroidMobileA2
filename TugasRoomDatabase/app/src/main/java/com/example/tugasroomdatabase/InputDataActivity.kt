package com.example.tugasroomdatabase

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tugasroomdatabase.database.DataPemilih
import com.example.tugasroomdatabase.database.DataPemilihDao
import com.example.tugasroomdatabase.database.DataPemilihRoomDatabase
import com.example.tugasroomdatabase.databinding.ActivityInputDataBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class InputDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputDataBinding
    private lateinit var mDataPemilihDao: DataPemilihDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi ExecutorService dan DAO
        executorService = Executors.newSingleThreadExecutor()
        val db = DataPemilihRoomDatabase.getDatabase(this)
        mDataPemilihDao = db!!.pemilihDao()!!

        with(binding) {
            btnInput.setOnClickListener {
                val gender = getSelectedGender()
                if (gender != null && checkAllField()) {
                    insertData(
                        DataPemilih(
                            nama_pemilih = namaPemilih.text.toString(),
                            nik = nik.text.toString().toIntOrNull() ?: 0,
                            gender = gender,
                            alamat = alamat.text.toString()
                        )
                    )
                    setEmptyField()
                    finish() // Kembali ke activity sebelumnya
                } else if (gender == null) {
                    Toast.makeText(this@InputDataActivity, "Pilih gender terlebih dahulu!", Toast.LENGTH_SHORT).show()
                }
            }

            btnBack.setOnClickListener {
                startActivity(Intent(this@InputDataActivity, HomeActivity::class.java))
                finish() // Tutup activity saat kembali
            }
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
            null
        }
    }

    /**
     * Fungsi untuk memasukkan data ke database
     */
    private fun insertData(dataPemilih: DataPemilih) {
        executorService.execute {
            mDataPemilihDao.insert(dataPemilih)
            runOnUiThread {
                Toast.makeText(this@InputDataActivity, "Data berhasil dimasukkan", Toast.LENGTH_SHORT).show()
            }
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
            gender.clearCheck()
        }
    }

    /**
     * Fungsi untuk memvalidasi semua field
     */
    private fun checkAllField(): Boolean {
        with(binding) {
            return if (namaPemilih.text?.toString()?.isEmpty() == true ||
                nik.text?.toString()?.isEmpty() == true ||
                alamat.text?.toString()?.isEmpty() == true) {
                Toast.makeText(this@InputDataActivity, "Harap isi semua data!", Toast.LENGTH_SHORT).show()
                false
            } else {
                true
            }
        }
    }
}
