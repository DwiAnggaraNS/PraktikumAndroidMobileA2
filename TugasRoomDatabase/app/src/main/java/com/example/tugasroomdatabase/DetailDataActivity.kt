package com.example.tugasroomdatabase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.tugasroomdatabase.databinding.ActivityDetailDataBinding
import com.example.tugasroomdatabase.databinding.ActivityInputDataBinding
import com.example.tugasroomdatabase.databinding.ActivityMainBinding

class DetailDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDataBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val namaPemilih = intent.getStringExtra("NAMA_PEMILIH")
        val nikPemilih = intent.getStringExtra("NIK_PEMILIH")
        val genderPemilih = intent.getStringExtra("GENDER_PEMILIH")
        val alamatPemilih = intent.getStringExtra("ALAMAT_PEMILIH")

        findViewById<TextView>(R.id.nama_pemilih).text = namaPemilih
        findViewById<TextView>(R.id.nik_detail).text = nikPemilih
        findViewById<TextView>(R.id.gender).text = genderPemilih
        findViewById<TextView>(R.id.alamat).text = alamatPemilih

        with(binding){
            btnBack.setOnClickListener {
                startActivity(Intent(this@DetailDataActivity, HomeActivity::class.java))
            }
        }
    }
}
