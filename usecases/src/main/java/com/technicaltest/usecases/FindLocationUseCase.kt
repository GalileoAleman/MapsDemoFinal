package com.technicaltest.usecases

import com.technicaltest.data.LocationRepository
import com.technicaltest.domain.LocationResult
import javax.inject.Inject

class FindLocationUseCase @Inject constructor(private val locationRepository: LocationRepository) {
    suspend operator fun invoke(): LocationResult = locationRepository.findLocation()
}
