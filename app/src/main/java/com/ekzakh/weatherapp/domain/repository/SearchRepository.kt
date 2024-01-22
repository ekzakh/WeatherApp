package com.ekzakh.weatherapp.domain.repository

import com.ekzakh.weatherapp.domain.entity.City

interface SearchRepository {
    suspend fun search(query: String): City
}
