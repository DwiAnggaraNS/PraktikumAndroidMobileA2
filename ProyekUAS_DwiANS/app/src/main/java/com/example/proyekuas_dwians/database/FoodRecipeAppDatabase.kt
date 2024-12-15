package com.example.proyekuas_dwians.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.proyekuas_dwians.database.SavedFoodRecipe

@Database(entities = [SavedFoodRecipe::class], version = 2)
abstract class FoodRecipeAppDatabase : RoomDatabase() {
    abstract fun foodRecipeDao(): DataFoodRecipeDAO

    companion object {
        @Volatile
        private var INSTANCE: FoodRecipeAppDatabase? = null

        fun getDatabase(context: Context): FoodRecipeAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodRecipeAppDatabase::class.java,
                    "food_recipe_database"
                )
                    .fallbackToDestructiveMigration() // Allow destructive migrations
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}