package com.example.meteo.core.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.meteo.core.location.LocationProvider
import com.example.meteo.data.repository.WeatherRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class WeatherRefreshWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: WeatherRepository,
    private val locationProvider: LocationProvider
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val (lat, lon) = locationProvider.getLastLocation() ?: return Result.retry()
        return runCatching {
            repository.refresh(lat, lon)
            enqueueNext(applicationContext)
            Result.success()
        }.getOrElse {
            Result.retry()
        }
    }

    companion object {
        private const val UNIQUE_NAME = "weather_refresh_5m"

        fun enqueueNext(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

            val request = OneTimeWorkRequestBuilder<WeatherRefreshWorker>()
                .setInitialDelay(5, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                UNIQUE_NAME,
                ExistingWorkPolicy.REPLACE,
                request
            )
        }
    }
}
