package com.example.proyekuas_dwians.network

import com.example.proyekuas_dwians.models.FoodRecipes
import com.example.proyekuas_dwians.models.Users
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

import okhttp3.RequestBody
import retrofit2.http.Body

interface ApiServices {
    // CRUD for Users
    @GET("user")
    fun getAllUsers(): Call<List<Users>>

    @GET("user/{id}")
    fun getUserById(@Path("id") bookId: String): Call<List<Users>>

    @POST("user")
    fun postNewUsers(@Body user: Users): Call<Users>


    // CRUD for Food-recipe
    @GET("food-recipe")
    fun getAllRecipes(): Call<List<FoodRecipes>>

    @POST("food-recipe")
    fun postNewRecipe (@Body rawJson: RequestBody): Call<FoodRecipes>

    @POST("food-recipe/{id}")
    fun updateRecipe (@Path("id") recipeId: String, @Body rawJson: RequestBody): Call<FoodRecipes>

    @DELETE("food-recipe/{id}")
    fun deleteRecipe (@Path("id") recipeId: String): Call<Void>
}