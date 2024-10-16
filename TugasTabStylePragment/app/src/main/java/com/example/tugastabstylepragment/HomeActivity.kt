package com.example.tugastabstylepragment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.example.tugastabstylepragment.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan binding untuk mengatur konten view
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Toolbar dan set sebagai Action Bar
        val toolbar: Toolbar = binding.myToolbar
        setSupportActionBar(toolbar)

        // Mengambil username dari Intent dan mengganti teks pada usernameView
        val username = intent.getStringExtra("USERNAME")
        binding.usernameView.text = username
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_exit -> {
                val intent = Intent(this@HomeActivity, MainActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}