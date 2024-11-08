package com.example.tugasretrofit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetailUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        // Ambil data dari intent
        val firstName = intent.getStringExtra("firstName")
        val lastName = intent.getStringExtra("lastName")
        val email = intent.getStringExtra("email")
        val avatar = intent.getStringExtra("avatar") // URL atau path gambar

        // Tampilkan data di UI
        findViewById<TextView>(R.id.tv_name).text = "$firstName $lastName"
        findViewById<TextView>(R.id.tv_email).text = email

        // Load image
        val imgProfile = findViewById<ImageView>(R.id.img_profile)
        Glide.with(this)
            .load(avatar)
            .into(imgProfile)

        setUpBottomNavigation()
    }

    fun setUpBottomNavigation(){
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_home -> {
                    intent = Intent(this@DetailUser, MainActivity::class.java)
                    startActivity(intent);
                    true
                }
                R.id.navigation_profile -> {
                    true
                }
                else -> false
            }
        }
    }
}