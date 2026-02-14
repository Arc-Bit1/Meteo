package com.example.meteo.data.repository

import com.example.meteo.BuildConfig
import com.example.meteo.data.local.WeatherCacheEntity
import com.example.meteo.data.local.WeatherDao
import com.example.meteo.data.remote.MeteoblueApi
import com.example.meteo.domain.model.CurrentWeather
import com.example.meteo.domain.model.DailyForecast
import com.example.meteo.domain.model.HourlyForecast
import com.example.meteo.domain.model.WeatherBundle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: MeteoblueApi,
    private val dao: WeatherDao
) {
    fun observeCachedCurrentWeather(): Flow<CurrentWeather?> = dao.observeCurrent().map { entity ->
        entity?.toDomain()
    }

    suspend fun refresh(lat: Double, lon: Double): WeatherBundle {
        val day = api.basicDay(apiKey = BuildConfig.METEOBLUE_API_KEY, lat = lat, lon = lon)
        val hourly = api.basic1h(apiKey = BuildConfig.METEOBLUE_API_KEY, lat = lat, lon = lon)

        val dayData = day.data_day ?: error("Réponse Meteoblue invalide: data_day manquant")
        val hourlyData = hourly.data_1h ?: error("Réponse Meteoblue invalide: data_1h manquant")

        val city = day.metadata?.name ?: "Position actuelle"
        val nowTemp = dayData.temperatureMean.firstOrNull() ?: 0.0
        val humidity = dayData.relativehumidityMean.firstOrNull() ?: 0
        val pressure = dayData.sealevelpressureMean.firstOrNull()?.toInt() ?: 0
        val wind = dayData.windspeedMean.firstOrNull() ?: 0.0
        val pictocode = dayData.pictocode.firstOrNull() ?: 1

        val currentDomain = CurrentWeather(
            city = city,
            temp = nowTemp,
            feelsLike = nowTemp,
            humidity = humidity,
            windSpeed = wind,
            pressure = pressure,
            description = pictocodeToDescription(pictocode),
            icon = pictocode.toString(),
            timestamp = Instant.now()
        )

        dao.upsert(
            WeatherCacheEntity(
                city = currentDomain.city,
                temp = currentDomain.temp,
                feelsLike = currentDomain.feelsLike,
                humidity = currentDomain.humidity,
                windSpeed = currentDomain.windSpeed,
                pressure = currentDomain.pressure,
                description = currentDomain.description,
                icon = currentDomain.icon,
                timestamp = currentDomain.timestamp.epochSecond
            )
        )

        val hourly24h = hourlyData.time.zip(hourlyData.temperature).zip(hourlyData.pictocode)
            .take(24)
            .map { (timeAndTemp, code) ->
                val (timeIso, temp) = timeAndTemp
                HourlyForecast(
                    time = parseMeteoblueTime(timeIso),
                    temp = temp,
                    icon = code.toString()
                )
            }

        val daily7d = dayData.time
            .indices
            .take(7)
            .map { i ->
                DailyForecast(
                    date = parseMeteoblueTime(dayData.time[i]),
                    minTemp = dayData.temperatureMin.getOrElse(i) { dayData.temperatureMean.getOrElse(i) { 0.0 } },
                    maxTemp = dayData.temperatureMax.getOrElse(i) { dayData.temperatureMean.getOrElse(i) { 0.0 } },
                    icon = dayData.pictocode.getOrElse(i) { pictocode }.toString()
                )
            }

        return WeatherBundle(current = currentDomain, hourly24h = hourly24h, daily7d = daily7d)
    }
}

private fun WeatherCacheEntity.toDomain(): CurrentWeather = CurrentWeather(
    city = city,
    temp = temp,
    feelsLike = feelsLike,
    humidity = humidity,
    windSpeed = windSpeed,
    pressure = pressure,
    description = description,
    icon = icon,
    timestamp = Instant.ofEpochSecond(timestamp)
)

private fun parseMeteoblueTime(value: String): Instant {
    return runCatching {
        if (value.contains("T")) {
            LocalDateTime.parse(value).toInstant(ZoneOffset.UTC)
        } else {
            LocalDateTime.parse("${value}T00:00").toInstant(ZoneOffset.UTC)
        }
    }.getOrElse { Instant.now() }
}

private fun pictocodeToDescription(code: Int): String = when (code) {
    1 -> "Ensoleillé"
    2, 3 -> "Partiellement nuageux"
    4, 5 -> "Nuageux"
    6, 7 -> "Couvert"
    8, 9, 10 -> "Pluie"
    11, 12 -> "Orage"
    13, 14, 15 -> "Neige"
    else -> "Conditions variables"
}
