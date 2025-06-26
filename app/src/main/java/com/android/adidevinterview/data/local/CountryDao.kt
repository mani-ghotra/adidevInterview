package com.android.adidevinterview.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountries(countries: List<CountryEntity>)

    @Query("SELECT * FROM countries ORDER BY originalPosition ASC")
    fun getCountriesOrdered(): Flow<List<CountryEntity>>

    @Query("""
        SELECT * FROM countries 
        WHERE name LIKE '%' || :query || '%' 
        OR region LIKE '%' || :query || '%'
        OR code LIKE :query
        OR capital LIKE '%' || :query || '%'
        ORDER BY originalPosition ASC
    """)
    fun searchCountriesOrdered(query: String): Flow<List<CountryEntity>>
}