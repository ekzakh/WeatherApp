package com.ekzakh.weatherapp.data.mapper

import com.ekzakh.weatherapp.data.local.model.CityDbModel
import com.ekzakh.weatherapp.domain.entity.City

fun City.toCityDbModel(): CityDbModel = CityDbModel(id, name, country)

fun CityDbModel.toEntity(): City = City(id, name, country)

fun List<CityDbModel>.toEntities(): List<City> = this.map { it.toEntity() }
