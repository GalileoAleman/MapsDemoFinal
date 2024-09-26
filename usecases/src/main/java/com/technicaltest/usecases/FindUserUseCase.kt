package com.technicaltest.usecases

import com.technicaltest.data.LoginRepository
import com.technicaltest.domain.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FindUserUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    operator fun invoke(id: Int): Flow<User> = loginRepository.findById(id)
}
