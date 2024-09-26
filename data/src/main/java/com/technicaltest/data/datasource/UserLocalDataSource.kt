package com.technicaltest.data.datasource

import com.technicaltest.domain.LoginResult
import com.technicaltest.domain.User
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
    suspend fun isEmpty(): Boolean
    fun findById(id: Int) : Flow<User>
    suspend fun save(user : User) : LoginResult
    suspend fun isUserLoggedIn(): Boolean
    suspend fun userSignOut()
}
