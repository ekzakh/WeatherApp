package com.ekzakh.weatherapp.domain.usecase

import com.ekzakh.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(private val repository: WeatherRepository) {
    suspend operator fun invoke(cityId: Int) = repository.currentWeather(cityId)
}
