package com.android.adidevinterview.data.datasource

import com.android.adidevinterview.data.remote.CountryApiService
import com.android.adidevinterview.data.remote.CountryResponse
import javax.inject.Inject

class CountryRemoteDataSource @Inject constructor(
    private val apiService: CountryApiService
) {
    suspend fun getCountries(): List<CountryResponse> {
        return apiService.getCountries()
    }
}