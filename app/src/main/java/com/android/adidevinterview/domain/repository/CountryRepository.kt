package com.android.adidevinterview.domain.repository

import com.android.adidevinterview.domain.model.Country
import com.android.adidevinterview.util.Resource
import kotlinx.coroutines.flow.Flow


interface CountryRepository {
    fun getCountries(): Flow<Resource<List<Country>>>
    fun searchCountries(query: String): Flow<List<Country>>
}