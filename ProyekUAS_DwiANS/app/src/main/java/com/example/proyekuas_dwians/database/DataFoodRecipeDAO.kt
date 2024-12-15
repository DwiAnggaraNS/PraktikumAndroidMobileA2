package com.example.proyekuas_dwians.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.proyekuas_dwians.models.FoodRecipes

@Dao
interface DataFoodRecipeDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(dataFoodRecipes: SavedFoodRecipe)
    @Update
    fun update(dataFoodRecipes: SavedFoodRecipe)
    @Delete
    fun delete(dataFoodRecipes: SavedFoodRecipe)

    @Query("SELECT * FROM saved_food_recipes WHERE userId = :userId ORDER BY idRecipe ASC")
    fun getSavedFoodRecipesByUser(userId: String): LiveData<List<SavedFoodRecipe>>

    @Query("SELECT * FROM saved_food_recipes WHERE idRecipe = :id AND userId = :idUser")
    suspend fun getSavedFoodRecipesById(id: String, idUser: String): SavedFoodRecipe?

    @Query("SELECT idRecipe FROM saved_food_recipes WHERE userId = :userId")
    suspend fun getlistOfSavedIdRecipeSavedFoodRecipesById(userId: String): List<String>?

}