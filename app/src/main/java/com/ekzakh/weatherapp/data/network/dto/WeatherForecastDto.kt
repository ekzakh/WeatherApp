package com.ekzakh.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherForecastDto(
    @SerializedName("current") val weather: WeatherDto,
    @SerializedName("forecast") val forecast: ForecastDto,
)
