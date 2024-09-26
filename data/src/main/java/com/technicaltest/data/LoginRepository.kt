package com.technicaltest.data

import com.technicaltest.data.datasource.UserLocalDataSource
import com.technicaltest.domain.LoginResult
import com.technicaltest.domain.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginRepository @Inject constructor(private val userLocalDataSource: UserLocalDataSource)  {
    suspend fun login(user : User) : LoginResult = userLocalDataSource.save(user)

    suspend fun getCurrentUser(): Boolean = userLocalDataSource.isUserLoggedIn()

    suspend fun signout() = userLocalDataSource.userSignOut()

    fun findById(id: Int): Flow<User> = userLocalDataSource.findById(id)
}
