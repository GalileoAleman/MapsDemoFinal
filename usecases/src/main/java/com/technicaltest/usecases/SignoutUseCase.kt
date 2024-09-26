package com.technicaltest.usecases

import com.technicaltest.data.LoginRepository
import javax.inject.Inject

class SignoutUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend operator fun invoke(): Unit = loginRepository.signout()
}
