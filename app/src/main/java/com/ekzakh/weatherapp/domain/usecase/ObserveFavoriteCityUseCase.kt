package com.ekzakh.weatherapp.domain.usecase

import com.ekzakh.weatherapp.domain.repository.FavoriteRepository
import javax.inject.Inject

class ObserveFavoriteCityUseCase @Inject constructor(private val repository: FavoriteRepository) {
    operator fun invoke(cityId: Int) = repository.observeIsFavorite(cityId)
}
