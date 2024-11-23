package com.example.tugasroomdatabase.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DataPemilihDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(dataPemilih: DataPemilih)
    @Update
    fun update(dataPemilih: DataPemilih)
    @Delete
    fun delete(dataPemilih: DataPemilih)
    @get:Query("SELECT * from dataPemilih_table ORDER BY id ASC")
    val allDataPemilih: LiveData<List<DataPemilih>>
}