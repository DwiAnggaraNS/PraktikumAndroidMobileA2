package com.example.tugasretrofit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasretrofit.databinding.ActivityMainBinding
import com.example.tugasretrofit.model.UserResponse
import com.example.tugasretrofit.network.ApiClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchUsers()
        setUpBottomNavigation()
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(emptyList()) { user ->
            // Intent untuk memulai DetailUserActivity
            val intent = Intent(this, DetailUser::class.java).apply {
                putExtra("firstName", user.firstName)
                putExtra("lastName", user.lastName)
                putExtra("email", user.email)
                putExtra("avatar", user.avatar)
            }
            startActivity(intent)
        }

        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = userAdapter
        }
    }

    private fun fetchUsers() {
        showLoading(true)
        val client = ApiClient.getInstance()
        client.getAllUsers().enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    response.body()?.data?.let { users ->
                        // Update data di adapter yang sudah ada
                        userAdapter.updateData(users)
                    } ?: showError("Response body is empty")
                } else {
                    showError("Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                showLoading(false)
                showError("Connection error: ${t.message}")
            }
        })
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvUsers.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
    }

    fun setUpBottomNavigation(){
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_home -> {
                    // Kita sudah di MainActivity, jadi tidak perlu melakukan apa-apa
                    true
                }
                R.id.navigation_profile -> {
                    Toast.makeText(this@MainActivity, "COMING SOON", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

}