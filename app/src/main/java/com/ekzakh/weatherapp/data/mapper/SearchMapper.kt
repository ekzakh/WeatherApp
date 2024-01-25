package com.ekzakh.weatherapp.data.mapper

import com.ekzakh.weatherapp.data.network.dto.CityDto
import com.ekzakh.weatherapp.domain.entity.City

fun CityDto.toEntity(): City = City(id, name, country)

fun List<CityDto>.toEntities(): List<City> = this.map { it.toEntity() }
