package com.android.adidevinterview.ui.countryList

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.adidevinterview.domain.usecase.GetCountriesUseCase
import com.android.adidevinterview.domain.usecase.SearchCountriesUseCase
import com.android.adidevinterview.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryListViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val searchCountriesUseCase: SearchCountriesUseCase
) : ViewModel() {

    private val _state = mutableStateOf(CountryListState())
    val state: MutableState<CountryListState> = _state

    private var searchJob: Job? = null


    fun loadCountries() {
        _state.value = _state.value.copy(isLoading = true)
        searchJob?.cancel()
        searchJob = null
        viewModelScope.launch {
            getCountriesUseCase().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            countries = resource.data?.sortedBy { it.originalPosition }
                                ?: emptyList(),
                            isLoading = false,
                            error = null
                        )
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = resource.message
                        )
                    }

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }

                }
            }
        }
    }

    fun onSearch(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300) // Debounce
            if (query.isBlank()) {
                loadCountries()
            } else {
                searchCountriesUseCase(query).collect { results ->
                    _state.value =
                        _state.value.copy(countries = results)
                }
            }
        }
    }
}