package com.android.adidevinterview.ui.countryList.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.adidevinterview.domain.model.Country

@Composable
fun CountryList(
    countries: List<Country>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(countries) { country ->
            CountryItem(country = country)
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = DividerDefaults.color
            )
        }
    }
}
