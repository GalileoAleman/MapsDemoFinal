package com.technicaltest.mapsdemo.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val email: String,
    val password: String,
    val loged: Boolean
)
