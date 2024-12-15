package com.example.proyekuas_dwians.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.proyekuas_dwians.R
import com.example.proyekuas_dwians.database.FoodRecipeAppDatabase
import com.example.proyekuas_dwians.database.FoodRecipeRepository
import com.example.proyekuas_dwians.databinding.ActivityDetailRecipeBinding
import com.example.proyekuas_dwians.models.FoodRecipes
import com.example.proyekuas_dwians.sharedPref.PrefManager
import com.example.proyekuas_dwians.user.HomeUserActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailRecipeBinding
    private lateinit var repository: FoodRecipeRepository
    private lateinit var prefManager: PrefManager
    private lateinit var foodRecipe: FoodRecipes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)
        repository = FoodRecipeRepository(FoodRecipeAppDatabase.getDatabase(this)!!.foodRecipeDao()!!)

        // Get the recipe details from the intent
        foodRecipe = FoodRecipes(
            idRecipe = intent.getStringExtra("RECIPE_ID") ?: "",
            recipeTitle = intent.getStringExtra("RECIPE_TITLE") ?: "",
            recipeDescription = intent.getStringExtra("RECIPE_DESCRIPTION") ?: "",
            recipeIngredients = intent.getStringExtra("RECIPE_INGREDIENTS") ?: "",
            recipeInstructions = intent.getStringExtra("RECIPE_INSTRUCTIONS") ?: "",
            estimatedCookingTime = intent.getStringExtra("RECIPE_EST_COOKING_TIME") ?: "",
            estimatedCalories = intent.getStringExtra("RECIPE_EST_CALORIES") ?: "",
            recipeImageLink = intent.getStringExtra("RECIPE_FOOD_IMAGE_LINK") ?: ""
        )

        setupUI()
        checkIfRecipeIsSaved()
    }

    private fun setupUI() {
        binding.foodTitle.text = foodRecipe.recipeTitle
        binding.foodDescription.text = foodRecipe.recipeDescription
        binding.foodIngredients.text = foodRecipe.recipeIngredients
        binding.foodInstructions.text = foodRecipe.recipeInstructions
        binding.textTimeCooking.text = foodRecipe.estimatedCookingTime
        binding.textCalories.text = foodRecipe.estimatedCalories

        Glide.with(this)
            .load(foodRecipe.recipeImageLink)
            .into(binding.foodImage)
        binding.btnLoveAddToFavorit.setOnClickListener {
            if (prefManager.getRole() == "User"){
                toggleSaveRecipe()
            }
        }

        binding.btnBack.setOnClickListener {
            if (prefManager.getRole() == "User"){
                startActivity(Intent(this@DetailRecipeActivity, HomeUserActivity::class.java))
            }
            else{
                startActivity(Intent(this@DetailRecipeActivity, HomeAdminActivity::class.java))
            }
        }
    }

    private fun checkIfRecipeIsSaved() {
        CoroutineScope(Dispatchers.IO).launch {
            val savedRecipe = foodRecipe.idRecipe?.let { repository.getSavedFoodRecipeById(it, prefManager.getIdUser()) }
            runOnUiThread {
                if (savedRecipe != null) {
                    binding.btnLoveAddToFavorit.setImageResource(R.drawable.icon_saved_filled)
                } else {
                    binding.btnLoveAddToFavorit.setImageResource(R.drawable.save_button)
                }
            }
        }
    }

    private fun toggleSaveRecipe() {
        repository.toggleSaveFoodRecipe(foodRecipe, prefManager.getIdUser()) { isSaved ->
            runOnUiThread {
                if (isSaved) {
                    Toast.makeText(this, "Recipe successfully saved", Toast.LENGTH_SHORT).show()
                    binding.btnLoveAddToFavorit.setImageResource(R.drawable.icon_saved_filled)
                } else {
                    Toast.makeText(this, "Recipe successfully unsaved", Toast.LENGTH_SHORT).show()
                    binding.btnLoveAddToFavorit.setImageResource(R.drawable.save_button)
                }
            }
        }
    }
}