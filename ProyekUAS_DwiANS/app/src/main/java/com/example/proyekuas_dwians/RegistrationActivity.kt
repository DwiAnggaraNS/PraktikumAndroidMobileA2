package com.example.proyekuas_dwians

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.example.proyekuas_dwians.databinding.ActivityRegistrationBinding
import com.example.proyekuas_dwians.models.Users
import com.example.proyekuas_dwians.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var btnSignup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // binding progressbar untuk animasi loading saat mengirimkan data akun
        progressBar = binding.progressBar
        btnSignup = binding.btnSignup


        with(binding) {
            btnSignup.setOnClickListener {
                val inputEmail = email.text.toString()
                val inputUsername = username.text.toString()
                val inputPassword = password.text.toString()

                if (checkAllField(inputEmail, inputUsername, inputPassword)) {
                    // Tambahkan logika sebelum memulai proses pembuatan akun:
                    btnSignup.isEnabled = false // Disable tombol signup saat loading
                    progressBar.visibility = View.VISIBLE // Menampilkan progress bar

                    checkIfAccountExists(inputEmail) { isEmailTaken ->
                        if (isEmailTaken) {
                            progressBar.visibility = View.GONE // Sembunyikan progress bar jika username ada
                            btnSignup.isEnabled = true // Enable tombol signup kembali
                            Toast.makeText(
                                this@RegistrationActivity,
                                "Email sudah terdaftar!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val hashedPassword = hashPassword(inputPassword)
                            createNewUser(inputEmail, inputUsername, hashedPassword)
                        }
                    }
                }
            }

            btnLogin.setOnClickListener {
                startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
            }
        }
    }

    /**
     * Fungsi untuk memvalidasi semua field
     */
    private fun checkAllField(inputEmail: String, inputUsername: String, inputPassword: String): Boolean {
        return if (inputEmail.isEmpty() || inputUsername.isEmpty() || inputPassword.isEmpty()) {
            Toast.makeText(this, "Harap isi semua data!", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    /**
     * Fungsi untuk memeriksa apakah username sudah ada
     */
    private fun checkIfAccountExists(inputEmail: String, callback: (Boolean) -> Unit) {
        ApiClient.getInstance().getAllUsers().enqueue(object : Callback<List<Users>> {
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                if (response.isSuccessful) {
                    val users = response.body() ?: emptyList()
                    val isEmailTaken = users.any { it.email == inputEmail }
                    callback(isEmailTaken)
                } else {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Gagal memeriksa username: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    callback(true) // Anggap email sudah ada jika gagal memeriksa
                }
            }

            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                Toast.makeText(
                    this@RegistrationActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                callback(true) // Anggap email sudah ada jika terjadi error
            }
        })
    }

    /**
     * Fungsi untuk membuat user baru melalui API
     */
    private fun createNewUser(inputEmail: String, inputUsername: String, hashedPassword: String) {
        progressBar.visibility = View.VISIBLE // Tampilkan loading saat memulai pembuatan user
        btnSignup.isEnabled = false // Nonaktifkan tombol signup

        val newUser = Users(
            email = inputEmail,
            username = inputUsername,
            password = hashedPassword,
            role = "User"
        )

        ApiClient.getInstance().postNewUsers(newUser).enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                progressBar.visibility = View.GONE // Sembunyikan loading setelah respons
                btnSignup.isEnabled = true // Aktifkan kembali tombol signup
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Registrasi berhasil!",
                        Toast.LENGTH_SHORT
                    ).show()
                    setEmptyField()
                    finish()
                } else {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Gagal registrasi: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                progressBar.visibility = View.GONE // Sembunyikan loading jika gagal
                btnSignup.isEnabled = true // Aktifkan kembali tombol signup
                Toast.makeText(
                    this@RegistrationActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    /**
     * Fungsi untuk hashing password menggunakan SHA-256
     */
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    /**
     * Fungsi untuk mengosongkan input field
     */
    private fun setEmptyField() {
        with(binding) {
            email.setText("")
            username.setText("")
            password.setText("")
        }
    }
}
