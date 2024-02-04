package com.ekzakh.weatherapp.presentation.search

import com.ekzakh.weatherapp.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {
    val model: StateFlow<SearchStore.State>

    fun changeSearchQuery(query: String)

    fun onSearchClick()

    fun onClickBack()

    fun onCityClick(city: City)
}
