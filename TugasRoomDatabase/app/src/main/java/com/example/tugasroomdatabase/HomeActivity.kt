package com.example.tugasroomdatabase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.DataPemilihAdapter
import com.example.PrefManager
import com.example.tugasroomdatabase.database.DataPemilih
import com.example.tugasroomdatabase.database.DataPemilihDao
import com.example.tugasroomdatabase.database.DataPemilihRoomDatabase
import com.example.tugasroomdatabase.databinding.ActivityHomeBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: DataPemilihViewModel
    private lateinit var adapter: DataPemilihAdapter
    private lateinit var prefManager: PrefManager
    private lateinit var mDataPemilihDao: DataPemilihDao
    private lateinit var executorService: ExecutorService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)

        // Inisialisasi ExecutorService
        executorService = Executors.newSingleThreadExecutor()

        val db = DataPemilihRoomDatabase.getDatabase(this)
        mDataPemilihDao = db!!.pemilihDao()!!

        val recyclerView = binding.rvDataPemilih
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProvider(this)[DataPemilihViewModel::class.java]
        viewModel.allData.observe(this) { dataList ->
            adapter = DataPemilihAdapter(dataList,
                onEditClicked = { data ->
                    val intent = Intent(this, EditDataActivity::class.java).apply {
                        putExtra("ID_PEMILIH", data.id)
                        putExtra("NAMA_PEMILIH", data.nama_pemilih)
                        putExtra("NIK_PEMILIH", data.nik.toString())
                        putExtra("GENDER_PEMILIH", data.gender)
                        putExtra("ALAMAT_PEMILIH", data.alamat)
                    }
                    startActivity(intent)
                },
                onDeleteClicked = { data ->
                    delete(data)
                },
                onDetailClicked = { data ->
                    val intent = Intent(this, DetailDataActivity::class.java).apply {
                        putExtra("NAMA_PEMILIH", data.nama_pemilih)
                        putExtra("NIK_PEMILIH", data.nik.toString())
                        putExtra("GENDER_PEMILIH", data.gender)
                        putExtra("ALAMAT_PEMILIH", data.alamat)
                    }
                    startActivity(intent)
                })
            recyclerView.adapter = adapter
        }


        binding.btnInput.setOnClickListener {
            startActivity(Intent(this@HomeActivity, InputDataActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            prefManager.setLoggedIn(false)
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
            finish()
        }
    }
    /**
     * Fungsi untuk menghapus data ke database
     */
    private fun delete(data_pemilih: DataPemilih) {
        executorService.execute { mDataPemilihDao.delete(data_pemilih) }
    }
}
