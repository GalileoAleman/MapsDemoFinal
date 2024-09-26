package com.technicaltest.mapsdemo.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE id = :id")
    fun findById(id : Int): Flow<User>

    @Query("SELECT COUNT(id) FROM users")
    suspend fun usersCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE loged = 1 LIMIT 1)")
    suspend fun isUserLoggedIn(): Boolean

    @Query("UPDATE users SET loged = 0 WHERE loged = 1")
    suspend fun logOutUser()
}