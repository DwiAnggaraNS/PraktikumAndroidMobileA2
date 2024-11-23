package com.example.tugasroomdatabase.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DataPemilih::class], version = 1, exportSchema = false)
abstract class DataPemilihRoomDatabase : RoomDatabase() {
    abstract fun pemilihDao(): DataPemilihDao?
    companion object {
        @Volatile
        private var INSTANCE: DataPemilihRoomDatabase? = null
        fun getDatabase(context: Context): DataPemilihRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(DataPemilihRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DataPemilihRoomDatabase::class.java, "pemilih_database"
                    )
                        .build()
                }
            }
            return INSTANCE
        }
    }
}