package com.example.restoranapp.application

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.restoranapp.dao.RestoDao
import com.example.restoranapp.model.Resto

@Database(entities = [Resto::class], version = 1, exportSchema = false)
abstract class RestoDatabase: RoomDatabase() {
    abstract fun restoDao(): RestoDao

    companion object {
        private var INSTANCE: RestoDatabase? = null

        fun getDatabase(context: Context): RestoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RestoDatabase::class.java,
                    "resto_database"
                )
                    .allowMainThreadQueries()
                    .build()

                INSTANCE = instance
                instance
            }

        }
    }
}