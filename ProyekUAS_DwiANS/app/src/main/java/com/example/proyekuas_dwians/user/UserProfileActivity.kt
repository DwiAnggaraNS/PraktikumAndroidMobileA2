package com.example.proyekuas_dwians.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyekuas_dwians.LoginActivity
import com.example.proyekuas_dwians.R
import com.example.proyekuas_dwians.databinding.ActivityHomeUserBinding
import com.example.proyekuas_dwians.databinding.ActivityUserProfileBinding
import com.example.proyekuas_dwians.sharedPref.PrefManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var prefManager: PrefManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)


        setUpBottomNavigation()

        with(binding){
            emailUser.text = prefManager.getEmail()
            username.text = prefManager.getUsername()

            binding.btnLogout.setOnClickListener {
                prefManager.setLoggedIn(false)
                startActivity(Intent(this@UserProfileActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun setUpBottomNavigation() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this@UserProfileActivity, HomeUserActivity::class.java))
                    true
                }
                R.id.navigation_saved -> {
                    // Handle saved recipes navigation
                    startActivity(Intent(this@UserProfileActivity, SavedFoodRecipeActivity::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    // Handle profile navigation
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.navigation_profile
    }
}