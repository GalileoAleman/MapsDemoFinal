package com.technicaltest.domain

sealed class LocationResult {
    data class Success(val latitude: Double, val longitude: Double) : LocationResult()
    data class Error(val message: String) : LocationResult()
}
