package com.example.restoranapp.repository

import com.example.restoranapp.dao.RestoDao
import com.example.restoranapp.model.Resto
import kotlinx.coroutines.flow.Flow

class RestoRepository(private val restoDao: RestoDao) {
    val allRestos: Flow<List<Resto>> = restoDao.getAllResto()
    suspend fun insertResto(resto: Resto){
        restoDao.insertResto(resto)
    }
    suspend fun deleteResto(resto: Resto){
        restoDao.deleteResto(resto)
    }
    suspend fun updateResto(resto: Resto){
        restoDao.updateResto(resto)
    }
}