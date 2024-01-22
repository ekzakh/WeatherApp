package com.ekzakh.weatherapp.domain.repository

import com.ekzakh.weatherapp.domain.entity.Forecast
import com.ekzakh.weatherapp.domain.entity.Weather

interface WeatherRepository {
    suspend fun currentWeather(): Weather
    suspend fun forecast(): Forecast
}
