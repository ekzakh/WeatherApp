package com.ekzakh.weatherapp.data.mapper

import com.ekzakh.weatherapp.data.network.dto.WeatherCurrentDto
import com.ekzakh.weatherapp.data.network.dto.WeatherDto
import com.ekzakh.weatherapp.data.network.dto.WeatherForecastDto
import com.ekzakh.weatherapp.domain.entity.Forecast
import com.ekzakh.weatherapp.domain.entity.Weather
import java.util.*

fun WeatherDto.toEntity(): Weather = Weather(
    temperature,
    condition.text,
    condition.iconUrl.correctUrl(),
    date.toCalendar(),
)

fun WeatherForecastDto.toEntity(): Forecast = Forecast(
    currentWeather = this.weather.toEntity(),
    upcoming = this.forecast.forecastDay.map { forecastDayDto ->
        val dayWeather = forecastDayDto.dayWeather
        Weather(
            dayWeather.temperature,
            conditionText = dayWeather.condition.text,
            conditionUrl = dayWeather.condition.iconUrl.correctUrl(),
            date = forecastDayDto.date.toCalendar(),
        )
    },
)

fun WeatherCurrentDto.toEntity(): Weather = this.weather.toEntity()

private fun Long.toCalendar(): Calendar =
    Calendar.getInstance().apply { time = Date(this@toCalendar * 1000) }

private fun String.correctUrl(): String =
    "https:$this".replace(oldValue = "64x64", newValue = "128x128")
