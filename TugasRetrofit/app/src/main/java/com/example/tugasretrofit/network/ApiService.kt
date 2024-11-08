package com.example.tugasretrofit.network

import com.example.tugasretrofit.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("users?page=2")
    fun getAllUsers(): Call<UserResponse>  // Ubah dari Users ke UserResponse
}
