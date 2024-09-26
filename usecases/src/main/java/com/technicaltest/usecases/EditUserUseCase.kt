package com.technicaltest.usecases

import com.technicaltest.data.LoginRepository
import com.technicaltest.domain.LoginResult
import com.technicaltest.domain.User
import javax.inject.Inject

class EditUserUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend operator fun invoke(user: User): LoginResult = loginRepository.login(user)
}
