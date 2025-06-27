package com.android.adidevinterview.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {

    @Transaction
    suspend fun upsertCountries(countries: List<CountryEntity>) {

        insertCountries(countries)
    }

    @Query("DELETE FROM countries")
    suspend fun clearAllCountries()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
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

    @Query("SELECT EXISTS(SELECT 1 FROM countries WHERE code = :code LIMIT 1)")
    suspend fun countryExists(code: String): Boolean
}