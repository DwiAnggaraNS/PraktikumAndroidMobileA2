package com.example.proyekuas_dwians.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity("food_recipes")
data class FoodRecipes(
    @PrimaryKey
    @SerializedName("_id")
    val idRecipe: String? = "", // Nullable untuk auto-generate ID
    @SerializedName("recipe_title")
    val recipeTitle: String,
    @SerializedName("recipe_image_link")
    val recipeImageLink: String,
    @SerializedName("recipe_description")
    val recipeDescription: String,
    @SerializedName("recipe_ingredients")
    val recipeIngredients: String,
    @SerializedName("recipe_instructions")
    val recipeInstructions: String,
    @SerializedName("estimated_cooking_time")
    val estimatedCookingTime: String,
    @SerializedName("estimated_calories")
    val estimatedCalories: String,
)

