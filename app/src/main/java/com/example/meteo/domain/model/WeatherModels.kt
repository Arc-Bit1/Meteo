package com.example.meteo.domain.model

import java.time.Instant

data class CurrentWeather(
    val city: String,
    val temp: Double,
    val feelsLike: Double,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Int,
    val description: String,
    val icon: String,
    val timestamp: Instant
)

data class HourlyForecast(
    val time: Instant,
    val temp: Double,
    val icon: String
)

data class DailyForecast(
    val date: Instant,
    val minTemp: Double,
    val maxTemp: Double,
    val icon: String
)

data class WeatherBundle(
    val current: CurrentWeather,
    val hourly24h: List<HourlyForecast>,
    val daily7d: List<DailyForecast>
)
