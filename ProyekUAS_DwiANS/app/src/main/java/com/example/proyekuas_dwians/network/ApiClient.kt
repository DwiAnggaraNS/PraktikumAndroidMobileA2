package com.example.proyekuas_dwians.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    fun getInstance(): ApiServices {
        val mHttpLoggingInterceptor = HttpLoggingInterceptor() .setLevel(HttpLoggingInterceptor.Level.BODY)

        val mOkHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(mHttpLoggingInterceptor) .build()

        val builder = Retrofit.Builder()
        .baseUrl("https://ppapb-a-api.vercel.app/FKPoB/")
            .addConverterFactory (GsonConverterFactory.create())
            .client(mOkHttpClient)
            .build()
        return builder.create(ApiServices::class.java)
    }
}