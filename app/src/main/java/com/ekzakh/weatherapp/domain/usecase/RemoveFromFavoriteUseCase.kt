package com.ekzakh.weatherapp.domain.usecase

import com.ekzakh.weatherapp.domain.repository.FavoriteRepository
import javax.inject.Inject

class RemoveFromFavoriteUseCase @Inject constructor(private val repository: FavoriteRepository) {
    suspend operator fun invoke(cityId: Int) = repository.removeToFavorite(cityId)
}
