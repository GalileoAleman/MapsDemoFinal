package com.technicaltest.mapsdemo.data.database

import com.technicaltest.data.datasource.UserLocalDataSource
import com.technicaltest.domain.LoginResult
import com.technicaltest.domain.User
import com.technicaltest.mapsdemo.common.fromDomainModel
import com.technicaltest.mapsdemo.common.toDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject



class UserRoomDataSource @Inject constructor(private val userDao: UserDao) : UserLocalDataSource {

    override suspend fun isEmpty(): Boolean = userDao.usersCount() == 0

    override fun findById(id: Int) : Flow<User> = userDao.findById(id).map { it.toDomainModel() }

    override suspend fun save(user : User) : LoginResult =
        runCatching { userDao.insertUser(user.fromDomainModel()) }.fold(
            onSuccess = { result ->
                LoginResult(success = true, errorMsg = null)
            },
            onFailure = {
                LoginResult(success = false, errorMsg = "Error en login")
            }
        )

    override suspend fun isUserLoggedIn(): Boolean {
        return try {
            userDao.isUserLoggedIn()
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun userSignOut() {
        try {
            userDao.logOutUser()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
