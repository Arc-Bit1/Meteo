package com.example.meteo.core.location

import android.annotation.SuppressLint
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocationProvider @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {
    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(): Pair<Double, Double>? {
        val location = fusedLocationProviderClient.lastLocation.await() ?: return null
        return location.latitude to location.longitude
    }
}
