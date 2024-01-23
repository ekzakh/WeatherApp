package com.ekzakh.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class DayWeatherDto(
    @SerializedName("avgtemp_c") val temperature: Float,
    @SerializedName("condition") val condition: ConditionDto,
)
