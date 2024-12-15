package com.example.proyekuas_dwians.admin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyekuas_dwians.LoginActivity
import com.example.proyekuas_dwians.R
import com.example.proyekuas_dwians.network.ApiClient
import com.example.proyekuas_dwians.databinding.ActivityHomeAdminBinding
import com.example.proyekuas_dwians.models.FoodRecipes
import com.example.proyekuas_dwians.sharedPref.PrefManager
import com.example.proyekuas_dwians.user.SavedFoodRecipeActivity
import com.example.proyekuas_dwians.user.UserProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeAdminBinding
    private lateinit var prefManager: PrefManager
    private lateinit var adapter: AdminFoodRecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)

        binding.rvDataResep.layoutManager = LinearLayoutManager(this)
        adapter = AdminFoodRecipeAdapter(
            onEditClicked = { recipe -> navigateToEditRecipe(recipe) },
            onDeleteClicked = { recipe -> deleteRecipe(recipe) },
            onDetailClicked = { recipe -> navigateToDetailRecipe(recipe) }
        )
        binding.rvDataResep.adapter = adapter

        binding.btnInput.setOnClickListener {
            startActivity(Intent(this@HomeAdminActivity, InputRecipeDataActivity::class.java))
        }

        fetchRecipes()
        setUpBottomNavigation()
    }

    override fun onResume() {
        super.onResume()
        fetchRecipes()
    }

    private fun fetchRecipes() {
        binding.progressBar.visibility = View.VISIBLE
        val apiService = ApiClient.getInstance()
        apiService.getAllRecipes().enqueue(object : Callback<List<FoodRecipes>> {
            override fun onResponse(call: Call<List<FoodRecipes>>, response: Response<List<FoodRecipes>>) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    response.body()?.let { recipes ->
                        adapter.submitList(recipes)
                    }
                } else {
                    Toast.makeText(this@HomeAdminActivity, "Failed to fetch recipes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<FoodRecipes>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@HomeAdminActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToEditRecipe(foodRecipe: FoodRecipes) {
        val intent = Intent(this, EditRecipeActivity::class.java).apply {
            putExtra("RECIPE_ID", foodRecipe.idRecipe)
            putExtra("RECIPE_TITLE", foodRecipe.recipeTitle)
            putExtra("RECIPE_DESCRIPTION", foodRecipe.recipeDescription)
            putExtra("RECIPE_INGREDIENTS", foodRecipe.recipeIngredients)
            putExtra("RECIPE_INSTRUCTIONS", foodRecipe.recipeInstructions)
            putExtra("RECIPE_EST_COOKING_TIME", foodRecipe.estimatedCookingTime)
            putExtra("RECIPE_EST_CALORIES", foodRecipe.estimatedCalories)
            putExtra("RECIPE_FOOD_IMAGE_LINK", foodRecipe.recipeImageLink)
        }
        startActivity(intent)
    }

    private fun navigateToDetailRecipe(foodRecipe: FoodRecipes) {
        val intent = Intent(this, DetailRecipeActivity::class.java).apply {
            putExtra("RECIPE_TITLE", foodRecipe.recipeTitle)
            putExtra("RECIPE_DESCRIPTION", foodRecipe.recipeDescription)
            putExtra("RECIPE_INGREDIENTS", foodRecipe.recipeIngredients)
            putExtra("RECIPE_INSTRUCTIONS", foodRecipe.recipeInstructions)
            putExtra("RECIPE_EST_COOKING_TIME", foodRecipe.estimatedCookingTime)
            putExtra("RECIPE_EST_CALORIES", foodRecipe.estimatedCalories)
            putExtra("RECIPE_FOOD_IMAGE_LINK", foodRecipe.recipeImageLink)
        }
        startActivity(intent)
    }

    private fun deleteRecipe(recipe: FoodRecipes) {
        val apiService = ApiClient.getInstance()

        // 1. Hapus data resep melalui API backend
        apiService.deleteRecipe(recipe.idRecipe!!).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@HomeAdminActivity, "Recipe deleted successfully", Toast.LENGTH_SHORT).show()

                    // 2. Hapus gambar dari external storage jika ada
                    recipe.recipeImageLink?.let { imageUri ->
                        if (deleteImageFromExternalStorage(Uri.parse(imageUri))) {
                            Toast.makeText(this@HomeAdminActivity, "Image deleted successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@HomeAdminActivity, "Failed to delete image", Toast.LENGTH_SHORT).show()
                        }
                    }

                    // 3. Refresh daftar resep
                    fetchRecipes()
                } else {
                    Toast.makeText(this@HomeAdminActivity, "Failed to delete recipe", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@HomeAdminActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteImageFromExternalStorage(imageUri: Uri): Boolean {
        return try {
            val resolver = contentResolver
            // Periksa apakah URI valid dan file ada
            if (resolver.delete(imageUri, null, null) > 0) {
                true // Gambar berhasil dihapus
            } else {
                false // Gambar tidak ditemukan atau gagal dihapus
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun setUpBottomNavigation() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Handle home navigation
                    true
                }
                R.id.navigation_add_data -> {
                    // Handle saved recipes navigation
                    startActivity(Intent(this@HomeAdminActivity, InputRecipeDataActivity::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    // Handle profile navigation
                    startActivity(Intent(this@HomeAdminActivity, AdminProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.navigation_home
    }

}