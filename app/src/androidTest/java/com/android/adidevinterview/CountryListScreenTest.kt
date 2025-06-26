package com.android.adidevinterview

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.android.adidevinterview.domain.model.Country
import com.android.adidevinterview.ui.countryList.CountryListScreen
import com.android.adidevinterview.ui.countryList.CountryListState
import com.android.adidevinterview.ui.countryList.CountryListViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class CountryListScreenTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createComposeRule()

    @MockK
    lateinit var viewModel: CountryListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        hiltRule.inject()
    }

    @Test
    fun shouldShowErrorWhenNoNetworkAndNoCache() {
        // Given

        val errorState = CountryListState(
            countries = emptyList(),
            isLoading = false,
            error = "No network and no cached data"

        )
        coEvery { viewModel.state } returns mutableStateOf(errorState)

        // When
        composeTestRule.setContent {
            CountryListScreen(viewModel)
        }

        // Then

        composeTestRule.onNodeWithText("No network and no cached data").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    @Test
    fun shouldShowCachedDataWithNetworkError() {
        // Given
        val cachedState = CountryListState(
            countries = listOf(
                Country("Cached1", "Region1", "Code1", "Capital1", 0)
            ),
            isLoading = false,
            error = null,
            searchQuery = "",
        )
        coEvery { viewModel.state } returns mutableStateOf(cachedState)

        // When
        composeTestRule.setContent {
            CountryListScreen(viewModel)
        }

        // Then
        composeTestRule.onNodeWithText("Network error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cached1").assertIsDisplayed()
    }

    @Test
    fun shouldShowErrorWhenResponseEmpty() {
        // Given
        val emptyState = CountryListState(
            countries = emptyList(),
            isLoading = false,
            error ="No data available",
            searchQuery = ""
        )
        coEvery { viewModel.state } returns mutableStateOf(emptyState)

        // When
        composeTestRule.setContent {
            CountryListScreen(viewModel)
        }

        // Then
        composeTestRule.onNodeWithText("No data available").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    @Test
    fun shouldShowLoadingInitially() {
        // Given
        val loadingState = CountryListState(isLoading = true)
        coEvery { viewModel.state } returns mutableStateOf(value = loadingState)

        // When
        composeTestRule.setContent {
            CountryListScreen(viewModel)
        }

        // Then
        composeTestRule.onNodeWithTag("LoadingIndicator").assertIsDisplayed()
    }
}