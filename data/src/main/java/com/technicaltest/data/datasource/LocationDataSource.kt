package com.technicaltest.data.datasource

import com.technicaltest.domain.LocationResult

interface LocationDataSource {
    suspend fun findLocation(): LocationResult
}
