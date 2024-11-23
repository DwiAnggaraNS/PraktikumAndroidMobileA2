package com.example.tugasroomdatabase.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dataPemilih_table")
data class DataPemilih(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int=0,
    @ColumnInfo(name = "nama_pemilih")
    val nama_pemilih: String,
    @ColumnInfo(name = "nik")
    val nik: Int,
    @ColumnInfo(name = "gender")
    val gender: String,
    @ColumnInfo(name = "alamat")
    val alamat: String
)
