package com.ekzakh.weatherapp.domain.entity

import java.util.Calendar

data class Weather(
    val temperature: Float,
    val conditionText: String,
    val conditionUrl: String,
    val data: Calendar
)
