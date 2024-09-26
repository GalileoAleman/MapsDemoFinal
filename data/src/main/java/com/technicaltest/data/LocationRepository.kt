package com.technicaltest.data

import com.technicaltest.data.datasource.LocationDataSource
import com.technicaltest.domain.LocationResult
import javax.inject.Inject

class LocationRepository @Inject constructor(private val locationDataSource: LocationDataSource, private val permissionChecker: PermissionChecker) {

    suspend fun findLocation(): LocationResult {
        return if (permissionChecker.check(PermissionChecker.Permission.FINE_LOCATION)) {
            locationDataSource.findLocation()
        } else {
            LocationResult.Error("No se pudo obtener la ubicación.")
        }
    }
}
