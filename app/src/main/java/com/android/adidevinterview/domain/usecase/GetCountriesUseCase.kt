package com.android.adidevinterview.domain.usecase

import com.android.adidevinterview.domain.model.Country
import com.android.adidevinterview.domain.repository.CountryRepository
import com.android.adidevinterview.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCountriesUseCase @Inject constructor(
    private val repository: CountryRepository
) {
    operator fun invoke(): Flow<Resource<List<Country>>> {
        return repository.getCountries()
    }
}