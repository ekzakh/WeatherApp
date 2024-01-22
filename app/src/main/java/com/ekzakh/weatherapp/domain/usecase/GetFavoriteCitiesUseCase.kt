package com.ekzakh.weatherapp.domain.usecase

import com.ekzakh.weatherapp.domain.repository.FavoriteRepository
import com.ekzakh.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class GetFavoriteCitiesUseCase @Inject constructor(private val repository: FavoriteRepository) {
    suspend operator fun invoke() = repository.favoriteCities
}
