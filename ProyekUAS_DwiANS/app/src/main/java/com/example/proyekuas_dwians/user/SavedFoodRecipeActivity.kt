package com.example.proyekuas_dwians.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyekuas_dwians.R
import com.example.proyekuas_dwians.admin.DetailRecipeActivity
import com.example.proyekuas_dwians.database.FoodRecipeAppDatabase
import com.example.proyekuas_dwians.database.FoodRecipeRepository
import com.example.proyekuas_dwians.database.SavedFoodRecipe
import com.example.proyekuas_dwians.databinding.ActivitySavedFoodRecipeBinding
import com.example.proyekuas_dwians.models.FoodRecipes
import com.example.proyekuas_dwians.sharedPref.PrefManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class SavedFoodRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedFoodRecipeBinding
    private val viewModel: DataFoodRecipeViewModel by viewModels()
    private lateinit var adapter: UserDataSavedFoodRecipeAdapter
    private lateinit var prefManager: PrefManager
    private lateinit var repository: FoodRecipeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedFoodRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)
        repository = FoodRecipeRepository(FoodRecipeAppDatabase.getDatabase(this)!!.foodRecipeDao()!!)

        setupRecyclerView()
        observeViewModel()

        val userId = prefManager.getIdUser()
        viewModel.setUserId(userId)

        setUpBottomNavigation()

        with(binding){
            backToHome.setOnClickListener {
                startActivity(Intent(this@SavedFoodRecipeActivity, HomeUserActivity::class.java))
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = UserDataSavedFoodRecipeAdapter(
            onUnSavedClicked = { recipe ->
                repository.toggleSaveFoodRecipeForSavedActivity(recipe, prefManager.getIdUser()) { isSaved ->
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
        binding.rvDataResep.layoutManager = LinearLayoutManager(this)
        binding.rvDataResep.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.allData.observe(this, Observer { recipes ->
            if (recipes != null) {
                adapter.submitList(recipes)
                binding.progressBar.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.VISIBLE
            }
        })
    }

    private fun navigateToDetailRecipe(foodRecipe: SavedFoodRecipe) {
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

    private fun setUpBottomNavigation() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this@SavedFoodRecipeActivity, HomeUserActivity::class.java))
                    true
                }
                R.id.navigation_saved -> {
                    // Handle saved recipes navigation
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this@SavedFoodRecipeActivity, UserProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.navigation_saved
    }

}