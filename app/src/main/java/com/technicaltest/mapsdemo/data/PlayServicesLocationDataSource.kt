package com.technicaltest.mapsdemo.data

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.technicaltest.data.datasource.LocationDataSource
import com.technicaltest.domain.LocationResult
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class PlayServicesLocationDataSource @Inject constructor(application: Application) :
    LocationDataSource {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    override suspend fun findLocation(): LocationResult = findLastLocation().toLocationResult()

    @SuppressLint("MissingPermission")
    private suspend fun findLastLocation(): Location =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnCompleteListener {
                    continuation.resume(it.result)
                }
        }

    private suspend fun Location?.toLocationResult(): LocationResult {
        return if (this != null) {
            LocationResult.Success(latitude = this.latitude, longitude = this.longitude)
        } else {
            LocationResult.Error("No se pudo obtener la ubicación.")
        }
    }
}
