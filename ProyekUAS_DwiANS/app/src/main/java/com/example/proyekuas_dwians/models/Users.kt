package com.example.proyekuas_dwians.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity("users")
data class Users(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("_id")
    val id_user: String? = null, // Nullable untuk auto-generate ID
    @SerializedName("email")
    val email: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("role")
    val role: String,
)
