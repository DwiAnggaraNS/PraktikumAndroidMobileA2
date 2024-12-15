package com.example.proyekuas_dwians.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.proyekuas_dwians.models.Users
import com.google.gson.annotations.SerializedName

@Entity(tableName = "saved_food_recipes")
data class SavedFoodRecipe(
    @PrimaryKey()
    val idRecipe: String, // ID otomatis dan tidak nullable
    @ColumnInfo(name = "userId")
    val userId: String,
    @ColumnInfo(name = "recipeTitle")
    val recipeTitle: String,
    @ColumnInfo(name = "recipeImageLink")
    val recipeImageLink: String,
    @ColumnInfo(name = "recipeDescription")
    val recipeDescription: String,
    @ColumnInfo(name = "recipeIngredients")
    val recipeIngredients: String,
    @ColumnInfo(name = "recipeInstructions")
    val recipeInstructions: String,
    @ColumnInfo(name = "estimatedCookingTime")
    val estimatedCookingTime: String,
    @ColumnInfo(name = "estimatedCalories")
    val estimatedCalories: String,
    @ColumnInfo(name = "isSaved")
    var isSaved: Boolean,
)
