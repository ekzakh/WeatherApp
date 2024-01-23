package com.ekzakh.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class ForecastDayDto(
    @SerializedName("day") val dayWeather: DayWeatherDto,
    @SerializedName("date_epoch") val date: Long,
)
