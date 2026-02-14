package com.example.meteo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_cache")
data class WeatherCacheEntity(
    @PrimaryKey val id: Int = 1,
    val city: String,
    val temp: Double,
    val feelsLike: Double,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Int,
    val description: String,
    val icon: String,
    val timestamp: Long
)
