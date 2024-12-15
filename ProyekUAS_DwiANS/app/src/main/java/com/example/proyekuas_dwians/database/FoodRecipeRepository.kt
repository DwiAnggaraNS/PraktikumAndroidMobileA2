package com.example.proyekuas_dwians.database

import com.example.proyekuas_dwians.models.FoodRecipes
import com.example.proyekuas_dwians.sharedPref.PrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class FoodRecipeRepository(private val dao: DataFoodRecipeDAO) {

    suspend fun getSavedFoodRecipeById(recipeId: String, idUser: String): SavedFoodRecipe? {
        return dao.getSavedFoodRecipesById(recipeId, idUser)
    }

    suspend fun getSavedRecipeIds(userId: String): List<String>? {
        return dao.getlistOfSavedIdRecipeSavedFoodRecipesById(userId)
    }

    fun saveFoodRecipe(dataFoodRecipes: SavedFoodRecipe, onComplete: () -> Unit) {
        Executors.newSingleThreadExecutor().execute {
            dao.insert(dataFoodRecipes)
            onComplete()
        }
    }

    fun deleteSavedFoodRecipe(savedRecipe: SavedFoodRecipe, onComplete: () -> Unit) {
        Executors.newSingleThreadExecutor().execute {
            dao.delete(savedRecipe)
            onComplete()
        }
    }

    fun toggleSaveFoodRecipe(
        recipe: FoodRecipes,
        userId: String,
        onComplete: (isSaved: Boolean) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val savedRecipe = recipe.idRecipe?.let { dao.getSavedFoodRecipesById(it, userId) }
            if (savedRecipe == null) {
                val newSavedRecipe = recipe.idRecipe?.let {
                    SavedFoodRecipe(
                        idRecipe = it,
                        recipeTitle = recipe.recipeTitle,
                        recipeImageLink = recipe.recipeImageLink,
                        recipeDescription = recipe.recipeDescription,
                        recipeIngredients = recipe.recipeIngredients,
                        recipeInstructions = recipe.recipeInstructions,
                        estimatedCookingTime = recipe.estimatedCookingTime,
                        estimatedCalories = recipe.estimatedCalories,
                        userId = userId,
                        isSaved = true
                    )
                }
                if (newSavedRecipe != null) {
                    dao.insert(newSavedRecipe)
                    onComplete(true)
                }
            } else {
                dao.delete(savedRecipe)
                onComplete(false)
            }
        }
    }

    fun toggleSaveFoodRecipeForSavedActivity(
        recipe: SavedFoodRecipe,
        userId: String,
        onComplete: (isSaved: Boolean) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val savedRecipe = recipe.idRecipe?.let { dao.getSavedFoodRecipesById(it, userId) }
            if (savedRecipe == null) {
                val newSavedRecipe = recipe.idRecipe?.let {
                    SavedFoodRecipe(
                        idRecipe = it,
                        recipeTitle = recipe.recipeTitle,
                        recipeImageLink = recipe.recipeImageLink,
                        recipeDescription = recipe.recipeDescription,
                        recipeIngredients = recipe.recipeIngredients,
                        recipeInstructions = recipe.recipeInstructions,
                        estimatedCookingTime = recipe.estimatedCookingTime,
                        estimatedCalories = recipe.estimatedCalories,
                        userId = userId,
                        isSaved = true
                    )
                }
                if (newSavedRecipe != null) {
                    dao.insert(newSavedRecipe)
                }
                onComplete(true)
            } else {
                dao.delete(savedRecipe)
                onComplete(false)
            }
        }
    }
}