package com.example.proyekuas_dwians

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.proyekuas_dwians.admin.HomeAdminActivity
import com.example.proyekuas_dwians.databinding.ActivityMainBinding
import com.example.proyekuas_dwians.models.Users
import com.example.proyekuas_dwians.network.ApiClient
import com.example.proyekuas_dwians.sharedPref.PrefManager
import com.example.proyekuas_dwians.user.HomeUserActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)
        progressBar = binding.progressBar

        // Cek apakah user sudah login sebelumnya
        if (prefManager.isLoggedIn()) {
            navigateToHome() // Jika sudah login, langsung ke HomeActivity
            return
        }

        with(binding) {
            btnLogin.setOnClickListener {
                val inputEmail = email.text.toString()
                val inputPassword = password.text.toString()

                if (checkInput(inputEmail, inputPassword)) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Mohon isi semua data",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    progressBar.visibility = View.VISIBLE // Tampilkan progress bar
                    validateUser(inputEmail, inputPassword)
                }
            }

            btnRegis.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
            }
        }
    }

    private fun checkInput(email: String, password: String): Boolean {
        return email.isEmpty() || password.isEmpty()
    }

    private fun validateUser(inputEmail: String, inputPassword: String) {
        val client = ApiClient.getInstance()
        val response = client.getAllUsers()

        response.enqueue(object : Callback<List<Users>> {
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                progressBar.visibility = View.GONE // Sembunyikan progress bar setelah respons
                if (response.isSuccessful && response.body() != null) {
                    var isUserFound = false
                    response.body()?.forEach { user ->
                        if (user.email == inputEmail) {
                            isUserFound = true
                            val hashedInputPassword = hashPassword(inputPassword)
                            if (user.password == hashedInputPassword) {
                                // Simpan session ke SharedPreferences
                                prefManager.setLoggedIn(true)
                                prefManager.saveUsername(user.username)
                                prefManager.savePassword(user.password)
                                prefManager.saveRole(user.role)
                                prefManager.saveEmail(user.email)
                                user.id_user?.let { prefManager.saveIdUser(it) }

                                // Navigasi ke home berdasarkan role
                                navigateToHome()
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Password salah!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            return@forEach
                        }
                    }
                    if (!isUserFound) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Email tidak ditemukan!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.e("API Error", "Response not successful or body is null")
                }
            }

            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                progressBar.visibility = View.GONE // Sembunyikan progress bar jika gagal
                Toast.makeText(
                    this@LoginActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun navigateToHome() {
        val intentToHome = if (prefManager.getRole() == "Admin") {
            Intent(this@LoginActivity, HomeAdminActivity::class.java)
        } else {
            Intent(this@LoginActivity, HomeUserActivity::class.java)
        }
        startActivity(intentToHome)
        finish()
    }

    /**
     * Fungsi untuk hashing password menggunakan SHA-256
     */
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}