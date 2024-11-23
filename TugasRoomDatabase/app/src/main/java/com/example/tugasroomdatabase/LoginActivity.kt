package com.example.tugasroomdatabase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.PrefManager
import com.example.tugasroomdatabase.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)

        // Cek apakah user sudah login sebelumnya
        if (prefManager.isLoggedIn()) {
            navigateToHome() // Jika sudah login, langsung ke HomeActivity
            return
        }

        // Cek apakah user sudah ada di SharedPreferences, jika belum buat
        createUserIfNotExists()

        with(binding) {
            btnLogin.setOnClickListener {
                val username = username.text.toString()
                val password = password.text.toString()
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Mohon isi semua data",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (isValidUsernamePassword()) {
                        prefManager.setLoggedIn(true)
                        navigateToHome()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Username atau password salah",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun isValidUsernamePassword(): Boolean {
        val username = prefManager.getUsername()
        val password = prefManager.getPassword()
        val inputUsername = binding.username.text.toString()
        val inputPassword = binding.password.text.toString()
        return username == inputUsername && password == inputPassword
    }

    private fun navigateToHome() {
        Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
        finish()
    }

    private fun createUserIfNotExists() {
        // Hanya membuat user jika belum ada username dan password
        if (prefManager.getUsername().isEmpty() && prefManager.getPassword().isEmpty()) {
            val username = "Dwi"
            val password = "123"
            prefManager.saveUsername(username)
            prefManager.savePassword(password)
        }
    }
}
