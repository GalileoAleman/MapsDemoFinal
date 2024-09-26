package com.technicaltest.usecases

import com.technicaltest.data.LoginRepository
import javax.inject.Inject

class CurrentUserUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend operator fun invoke(): Boolean = loginRepository.getCurrentUser()
}
