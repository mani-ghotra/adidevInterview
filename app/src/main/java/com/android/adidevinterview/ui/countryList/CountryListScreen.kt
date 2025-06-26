package com.android.adidevinterview.ui.countryList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.adidevinterview.ui.countryList.components.CountryList
import com.android.adidevinterview.ui.countryList.components.EmptyView
import com.android.adidevinterview.ui.countryList.components.ErrorView
import com.android.adidevinterview.ui.countryList.components.LoadingView
import com.android.adidevinterview.ui.countryList.components.SearchBar

@Composable
fun CountryListScreen(viewModel: CountryListViewModel = hiltViewModel()) {
    val state by viewModel.state

    Scaffold(topBar = {
        SearchBar(
            query = state.searchQuery,
            onQueryChange = viewModel::onSearch,
            modifier = Modifier.fillMaxWidth()
        )
    }) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> LoadingView(Modifier.align(Alignment.Center))
                state.error != null -> ErrorView(
                    message = state.error!!,
                    modifier = Modifier.align(Alignment.Center),
                    onRetry = { viewModel.loadCountries() }
                )
                state.countries.isEmpty() -> EmptyView(Modifier.align(Alignment.Center))
                else -> CountryList(
                    countries = state.countries,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}