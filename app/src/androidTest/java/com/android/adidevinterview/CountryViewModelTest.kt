package com.android.adidevinterview

import androidx.compose.runtime.mutableStateOf
import com.android.adidevinterview.domain.model.Country
import com.android.adidevinterview.domain.repository.CountryRepository
import com.android.adidevinterview.domain.usecase.GetCountriesUseCase
import com.android.adidevinterview.domain.usecase.SearchCountriesUseCase
import com.android.adidevinterview.ui.countryList.CountryListState
import com.android.adidevinterview.ui.countryList.CountryListViewModel
import com.android.adidevinterview.util.Resource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class CountryViewModelTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @MockK
    lateinit var getCountriesUseCase: GetCountriesUseCase

    @MockK
    lateinit var searchCountriesUseCase: SearchCountriesUseCase

    private lateinit var viewModel: CountryListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        hiltRule.inject()
        viewModel = CountryListViewModel(
            getCountriesUseCase,
            searchCountriesUseCase
        )
    }

    @Test
    fun loadCountries_shouldUpdateStateCorrectlyForSuccess() = runTest {
        // Given
        val testCountries = listOf(
            Country("Test1", "Region1", "Code1", "Capital1", 0)
        )
        coEvery { getCountriesUseCase() } returns flowOf(
            Resource.Loading(),
            Resource.Success(testCountries)
        )

        // When
        viewModel.loadCountries()
        val state = viewModel.state.value

        // Then
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertEquals(1, state.countries.size)
    }

    @Test
    fun loadCountries_shouldShowErrorWhenNoNetworkAndNoCache() = runTest {
        // Given
        coEvery { getCountriesUseCase() } returns flowOf(
            Resource.Loading(),
            Resource.Error("No network and no cache")
        )

        // When
        viewModel.loadCountries()
        val state = viewModel.state.value

        // Then
        assertFalse(state.isLoading)
        assertEquals("No network and no cache", state.error)
        assertTrue(state.countries.isEmpty())
    }

    @Test
    fun loadCountries_shouldShowCachedDataWithErrorWhenNetworkFails() = runTest {
        // Given
        val testCountries = listOf(
            Country("Cached1", "Region1", "Code1", "Capital1", 0)
        )
        coEvery { getCountriesUseCase() } returns flowOf(
            Resource.Loading(),
            Resource.Success(testCountries),
            Resource.Error("Network error")
        )

        // When
        viewModel.loadCountries()
        val state = viewModel.state.value

        // Then
        assertFalse(state.isLoading)
        assertEquals("Network error", state.error)
        assertEquals(1, state.countries.size)
    }

    @Test
    fun loadCountries_shouldShowErrorWhenResponseIsEmpty() = runTest {
        // Given
        coEvery { getCountriesUseCase() } returns flowOf(
            Resource.Loading(),
            Resource.Error("No data available")
        )

        // When
        viewModel.loadCountries()
        val state = viewModel.state.value

        // Then
        assertFalse(state.isLoading)
        assertEquals("No data available", state.error)
        assertTrue(state.countries.isEmpty())
    }
}