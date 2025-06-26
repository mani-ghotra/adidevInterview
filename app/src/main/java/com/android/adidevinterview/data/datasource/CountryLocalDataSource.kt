package com.android.adidevinterview.data.datasource

import com.android.adidevinterview.data.local.CountryDao
import com.android.adidevinterview.data.local.CountryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CountryLocalDataSource @Inject constructor(
    private val countryDao: CountryDao
) {
    fun getCountries(): Flow<List<CountryEntity>> = countryDao.getCountriesOrdered()

    suspend fun cacheCountries(countries: List<CountryEntity>) {
        countryDao.insertCountries(countries)
    }

    fun searchCountries(query: String): Flow<List<CountryEntity>> {
        return countryDao.searchCountriesOrdered(query)
    }
}