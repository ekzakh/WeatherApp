package com.ekzakh.weatherapp.domain.usecase

import com.ekzakh.weatherapp.domain.entity.City
import com.ekzakh.weatherapp.domain.repository.FavoriteRepository
import com.ekzakh.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class AddToFavoriteUseCase @Inject constructor(private val repository: FavoriteRepository) {
    suspend operator fun invoke(city: City) = repository.addToFavorite(city)
}
