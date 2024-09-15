package com.example.tugaspertemuan4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tugaspertemuan4.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.submit.setOnClickListener {
            handleRegistration()
        }

        binding.buttonLogin.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun handleRegistration() {
        val username = binding.username.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val phone = binding.phone.text.toString().trim()
        val password = binding.password.text.toString().trim()

        if (validateInputs(username, email, phone, password)) {
            sendDataToLoginActivity(username, email, phone, password)
            Toast.makeText(this, "Register successfully. Redirecting to login page", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(username: String, email: String, phone: String, password: String): Boolean {
        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun sendDataToLoginActivity(username: String, email: String, phone: String, password: String) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("USERNAME", username)
        intent.putExtra("EMAIL", email)
        intent.putExtra("PHONE", phone)
        intent.putExtra("PASSWORD", password)
        startActivity(intent)
        finish() // Optional: closes the RegisterActivity so user can't go back to it using back button
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}