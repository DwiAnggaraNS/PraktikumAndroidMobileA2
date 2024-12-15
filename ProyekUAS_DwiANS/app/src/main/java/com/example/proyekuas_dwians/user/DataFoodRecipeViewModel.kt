package com.example.proyekuas_dwians.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.proyekuas_dwians.database.DataFoodRecipeDAO
import com.example.proyekuas_dwians.database.FoodRecipeAppDatabase
import com.example.proyekuas_dwians.database.SavedFoodRecipe

class DataFoodRecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: DataFoodRecipeDAO = FoodRecipeAppDatabase.getDatabase(application)!!.foodRecipeDao()!!

    // LiveData untuk userId
    private val userIdLiveData = MutableLiveData<String>()

    // MediatorLiveData untuk hasil query berdasarkan userId
    val allData: MediatorLiveData<List<SavedFoodRecipe>> = MediatorLiveData()

    init {
        // Mengamati perubahan userId dan memperbarui data
        allData.addSource(userIdLiveData) { userId ->
            if (userId != null) {
                allData.addSource(dao.getSavedFoodRecipesByUser(userId)) { recipes ->
                    allData.value = recipes
                }
            }
        }
    }

    // Method untuk memperbarui userId
    fun setUserId(userId: String) {
        userIdLiveData.value = userId
    }
}
