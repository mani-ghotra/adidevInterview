package com.android.adidevinterview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.android.adidevinterview.ui.countryList.CountryListScreen
import com.android.adidevinterview.ui.theme.AdidevInterviewTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdidevInterviewTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    CountryListScreen()
                }
            }
        }
    }
}
