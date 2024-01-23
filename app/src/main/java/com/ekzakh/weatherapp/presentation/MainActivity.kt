package com.ekzakh.weatherapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ekzakh.weatherapp.data.network.api.ApiFactory
import com.ekzakh.weatherapp.data.network.api.ApiService
import com.ekzakh.weatherapp.presentation.ui.theme.WeatherAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService = ApiFactory.create(ApiService::class.java)

        CoroutineScope(Dispatchers.Main).launch {
            val weather = apiService.currentWeather("London")
            val forecast = apiService.forecast("London")
            val search = apiService.searchCity("London")
            Log.d("TAG", "$weather \n$forecast \n$search")
        }
        setContent {
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                }
            }
        }
    }
}
