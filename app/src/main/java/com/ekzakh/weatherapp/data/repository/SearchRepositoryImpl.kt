package com.ekzakh.weatherapp.data.repository

import com.ekzakh.weatherapp.data.mapper.toEntity
import com.ekzakh.weatherapp.data.network.api.ApiService
import com.ekzakh.weatherapp.domain.entity.City
import com.ekzakh.weatherapp.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : SearchRepository {
    override suspend fun search(query: String): List<City> =
        apiService.searchCity(query).map { it.toEntity() }
}
