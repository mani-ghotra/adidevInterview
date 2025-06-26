package com.android.adidevinterview.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val region: String,
    val code: String,
    val capital: String,
    val originalPosition: Int  )