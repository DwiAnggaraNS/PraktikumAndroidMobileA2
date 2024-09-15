package com.example.tugaspertemuan4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tugaspertemuan4.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    // Variabel untuk menyimpan data yang diterima dari RegisterActivity
    private var registeredUsername: String? = null
    private var registeredEmail: String? = null
    private var registeredPhone: String? = null
    private var registeredPassword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menerima data dari RegisterActivity
        receiveRegistrationData()

        setupListeners()
    }

    private fun receiveRegistrationData() {
        registeredUsername = intent.getStringExtra("USERNAME")
        registeredEmail = intent.getStringExtra("EMAIL")
        registeredPhone = intent.getStringExtra("PHONE")
        registeredPassword = intent.getStringExtra("PASSWORD")

        // Log data yang diterima (untuk debugging)
        println("Received data: username = $registeredUsername | email = $registeredEmail | phone = $registeredPhone | password = $registeredPassword")
    }

    private fun setupListeners() {
        binding.buttonRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.login.setOnClickListener {
            handleLoginData()
        }
    }

    private fun handleLoginData() {
        val username = binding.username.text.toString().trim()
        val password = binding.password.text.toString().trim()

        if (validateLoginData(username, password)) {
            navigateToHome(username, registeredEmail, registeredPhone)
        } else {
            Toast.makeText(this, "Invalid login credentials", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateLoginData(username: String, password: String): Boolean {
        // Validasi login berdasarkan data yang diterima dari RegisterActivity
        return username == registeredUsername && password == registeredPassword
    }

    private fun navigateToHome(username: String, email: String?, phone: String?) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("USERNAME", username)
        intent.putExtra("EMAIL", email)
        intent.putExtra("PHONE", phone)
        startActivity(intent)
        finish() // Optional: menutup LoginActivity agar tidak bisa kembali dengan tombol back
    }
}