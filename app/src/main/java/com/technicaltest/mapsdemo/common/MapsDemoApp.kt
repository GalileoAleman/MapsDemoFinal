package com.technicaltest.mapsdemo.common

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MapsDemoApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}