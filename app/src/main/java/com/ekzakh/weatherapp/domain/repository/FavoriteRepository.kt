package com.ekzakh.weatherapp.domain.repository

import com.ekzakh.weatherapp.domain.entity.City
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    val favoriteCities: Flow<List<City>>

    fun observeIsFavorite(cityId: Int): Flow<Boolean>

    suspend fun addToFavorite(city: City)

    suspend fun removeToFavorite(cityId: Int)
}
