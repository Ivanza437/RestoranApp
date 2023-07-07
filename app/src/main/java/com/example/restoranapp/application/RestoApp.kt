package com.example.restoranapp.application

import android.app.Application
import com.example.restoranapp.repository.RestoRepository

class RestoApp: Application() {
    val database by lazy { RestoDatabase.getDatabase(this) }
    val repository by lazy { RestoRepository(database.restoDao()) }
}