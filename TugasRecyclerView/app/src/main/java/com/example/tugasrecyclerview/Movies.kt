package com.example.tugasrecyclerview

data class Movies(
    val movieTitle: String = "",
    val movieDuration: String = "",
    val movieAgeGroup: String = "",
    val moviePoster: Int = 0,  // Use Int for drawable resource IDs
    val movieSinopsis: String = ""
)
