package com.example.restoranapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.restoranapp.model.Resto
import kotlinx.coroutines.flow.Flow

@Dao
interface RestoDao {
    @Query("SELECT * FROM `resto_table` ORDER BY name ASC")
    fun getAllResto(): Flow<List<Resto>>

    @Insert
    suspend fun insertResto(resto: Resto)

    @Delete
    suspend fun deleteResto(resto: Resto)

    @Update fun updateResto(resto: Resto)
}