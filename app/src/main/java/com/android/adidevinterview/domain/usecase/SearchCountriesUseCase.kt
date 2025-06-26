package com.android.adidevinterview.domain.usecase

import com.android.adidevinterview.domain.model.Country
import com.android.adidevinterview.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchCountriesUseCase @Inject constructor(
    private val repository: CountryRepository
) {
    operator fun invoke(query: String): Flow<List<Country>> {
        return repository.searchCountries(query)
    }
}