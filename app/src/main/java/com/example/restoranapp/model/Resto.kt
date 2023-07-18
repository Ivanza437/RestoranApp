package com.example.restoranapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "resto_table")
data class Resto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val kind: String,
    val price: String,
    val latitude: Double?,
    val longitude: Double?

) : Parcelable