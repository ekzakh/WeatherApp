package com.ekzakh.weatherapp.data.repository

import com.ekzakh.weatherapp.data.local.db.FavoriteCitiesDao
import com.ekzakh.weatherapp.data.mapper.toCityDbModel
import com.ekzakh.weatherapp.data.mapper.toEntities
import com.ekzakh.weatherapp.domain.entity.City
import com.ekzakh.weatherapp.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteCitiesDao: FavoriteCitiesDao,
) : FavoriteRepository {

    override val favoriteCities: Flow<List<City>> =
        favoriteCitiesDao.favoriteCities().map { it.toEntities() }

    override fun observeIsFavorite(cityId: Int): Flow<Boolean> =
        favoriteCitiesDao.observeIsFavorite(cityId)

    override suspend fun addToFavorite(city: City) =
        favoriteCitiesDao.addToFavorite(city.toCityDbModel())

    override suspend fun removeToFavorite(cityId: Int) =
        favoriteCitiesDao.removeFromFavorite(cityId)
}
