package com.ekzakh.weatherapp.data.repository

import com.ekzakh.weatherapp.data.mapper.toEntity
import com.ekzakh.weatherapp.data.network.api.ApiService
import com.ekzakh.weatherapp.domain.entity.Forecast
import com.ekzakh.weatherapp.domain.entity.Weather
import com.ekzakh.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    WeatherRepository {
    override suspend fun currentWeather(cityId: Int): Weather =
        apiService.currentWeather("$PREFIX_CITY_ID$cityId").toEntity()

    override suspend fun forecast(cityId: Int): Forecast =
        apiService.forecast("$PREFIX_CITY_ID$cityId").toEntity()

    companion object {
        private const val PREFIX_CITY_ID = "id:"
    }
}
