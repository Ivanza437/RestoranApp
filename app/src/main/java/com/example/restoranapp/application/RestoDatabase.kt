package com.example.restoranapp.application

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.restoranapp.dao.RestoDao
import com.example.restoranapp.model.Resto

@Database(entities = [Resto::class], version = 2, exportSchema = false)
abstract class RestoDatabase: RoomDatabase() {
    abstract fun restoDao(): RestoDao

    companion object {
        private var INSTANCE: RestoDatabase? = null

        //migrasi database dari versi 1 ke 2, karena ada perubahan table tadi

        private val migration1To2: Migration = object: Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE resto_table ADD COLUMN latitude Double DEFAULT 0.0")
                database.execSQL("ALTER TABLE resto_table ADD COLUMN longitude Double DEFAULT 0.0")
            }

        }

        fun getDatabase(context: Context): RestoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RestoDatabase::class.java,
                    "resto_database"
                )
                    .addMigrations(migration1To2)
                    .allowMainThreadQueries()
                    .build()

                INSTANCE = instance
                instance
            }

        }
    }
}