package com.ekzakh.weatherapp.domain.usecase

import com.ekzakh.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(private val repository: WeatherRepository) {
    suspend operator fun invoke() = repository.forecast()
}
