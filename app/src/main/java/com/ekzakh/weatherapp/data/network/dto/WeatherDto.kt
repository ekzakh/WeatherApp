package com.ekzakh.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("last_updated_epoch") val date: Long,
    @SerializedName("temp_c") val temperature: Float,
    @SerializedName("condition") val condition: ConditionDto,
)
