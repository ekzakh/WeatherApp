package com.ekzakh.weatherapp.presentation.favorite

import com.ekzakh.weatherapp.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface FavoriteComponent {
    val model: StateFlow<FavoriteStore.State>

    fun clickCityItem(city: City)

    fun addToFavoriteClick()

    fun searchClick()
}
