package com.example.meteo.presentation.main

import android.content.Context
import com.example.meteo.core.worker.WeatherRefreshWorker

fun scheduleWeatherRefresh(context: Context) {
    WeatherRefreshWorker.enqueueNext(context)
}
