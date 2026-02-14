package com.example.meteo.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface MeteoblueApi {

    /**
     * Meteoblue free weather endpoint (basic-day) for current + daily forecast.
     */
    @GET("packages/basic-day")
    suspend fun basicDay(
        @Query("apikey") apiKey: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("asl") altitude: Int = 0,
        @Query("format") format: String = "json"
    ): MeteoblueDayDto

    /**
     * Meteoblue free weather endpoint (basic-1h) for hourly forecast.
     */
    @GET("packages/basic-1h")
    suspend fun basic1h(
        @Query("apikey") apiKey: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("asl") altitude: Int = 0,
        @Query("format") format: String = "json"
    ): MeteoblueHourlyDto
}
