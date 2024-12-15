package com.example.proyekuas_dwians.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyekuas_dwians.LoginActivity
import com.example.proyekuas_dwians.R
import com.example.proyekuas_dwians.admin.DetailRecipeActivity
import com.example.proyekuas_dwians.database.FoodRecipeAppDatabase
import com.example.proyekuas_dwians.database.FoodRecipeRepository
import com.example.proyekuas_dwians.sharedPref.PrefManager
import com.example.proyekuas_dwians.databinding.ActivityHomeUserBinding
import com.example.proyekuas_dwians.models.FoodRecipes
import com.example.proyekuas_dwians.network.ApiClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeUserBinding
    private lateinit var prefManager: PrefManager
    private lateinit var adapter: UserDataFoodRecipeAdapter
    private lateinit var repository: FoodRecipeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)
        repository = FoodRecipeRepository(FoodRecipeAppDatabase.getDatabase(this)!!.foodRecipeDao()!!)

        setupUI()
        fetchRecipes()
        fetchSavedRecipeIds()
        setUpBottomNavigation()

        binding.greetingUsernameUser.text = prefManager.getUsername()
    }

    private fun setupUI() {
        binding.rvRecipes.layoutManager = LinearLayoutManager(this)
        adapter = UserDataFoodRecipeAdapter(
            onSaveClicked = { recipe ->
                repository.toggleSaveFoodRecipe(recipe, prefManager.getIdUser()) { isSaved ->
                    runOnUiThread {
                        if (isSaved) {
                            Toast.makeText(this, "Recipe successfully saved", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Recipe successfully unsaved", Toast.LENGTH_SHORT).show()
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            },
            onDetailClicked = { recipe -> navigateToDetailRecipe(recipe) }
        )
        binding.rvRecipes.adapter = adapter
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
                    Toast.makeText(this@HomeUserActivity, "Failed to fetch recipes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<FoodRecipes>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@HomeUserActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchSavedRecipeIds() {
        CoroutineScope(Dispatchers.IO).launch {
            val savedRecipeIds = repository.getSavedRecipeIds(prefManager.getIdUser())
            runOnUiThread {
                if (savedRecipeIds != null) {
                    adapter.setSavedRecipes(savedRecipeIds)
                }
            }
        }
    }

    private fun navigateToDetailRecipe(foodRecipe: FoodRecipes) {
        val intent = Intent(this, DetailRecipeActivity::class.java).apply {
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

    override fun onResume() {
        super.onResume()
        fetchRecipes()
    }

    private fun setUpBottomNavigation() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Handle home navigation
                    true
                }
                R.id.navigation_saved -> {
                    // Handle saved recipes navigation
                    startActivity(Intent(this@HomeUserActivity, SavedFoodRecipeActivity::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    // Handle profile navigation
                     startActivity(Intent(this@HomeUserActivity, UserProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.navigation_home
    }
}