package com.example.tugasroomdatabase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.tugasroomdatabase.database.DataPemilih
import com.example.tugasroomdatabase.database.DataPemilihDao
import com.example.tugasroomdatabase.database.DataPemilihRoomDatabase

class DataPemilihViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: DataPemilihDao = DataPemilihRoomDatabase.getDatabase(application)!!.pemilihDao()!!
    val allData: LiveData<List<DataPemilih>> = dao.allDataPemilih
}
