package com.android.adidevinterview.ui.countryList

import com.android.adidevinterview.domain.model.Country

data class CountryListState(
    val countries: List<Country> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)