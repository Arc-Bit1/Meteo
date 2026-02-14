package com.example.meteo.data.remote

import com.squareup.moshi.Json

data class MeteoblueMetadataDto(
    val name: String? = null
)

data class MeteoblueDayDataDto(
    val time: List<String> = emptyList(),
    @Json(name = "temperature_mean") val temperatureMean: List<Double> = emptyList(),
    @Json(name = "temperature_min") val temperatureMin: List<Double> = emptyList(),
    @Json(name = "temperature_max") val temperatureMax: List<Double> = emptyList(),
    val relativehumidityMean: List<Int> = emptyList(),
    val sealevelpressureMean: List<Double> = emptyList(),
    @Json(name = "windspeed_mean") val windspeedMean: List<Double> = emptyList(),
    val pictocode: List<Int> = emptyList()
)

data class MeteoblueDayDto(
    val metadata: MeteoblueMetadataDto? = null,
    val data_day: MeteoblueDayDataDto? = null
)

data class Meteoblue1hDataDto(
    val time: List<String> = emptyList(),
    val temperature: List<Double> = emptyList(),
    val pictocode: List<Int> = emptyList()
)

data class MeteoblueHourlyDto(
    val data_1h: Meteoblue1hDataDto? = null
)
