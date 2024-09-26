package com.technicaltest.mapsdemo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class MapsDemoDataBase : RoomDatabase(){
    abstract fun userDao(): UserDao
}
