package com.technicaltest.mapsdemo.data

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.technicaltest.data.PermissionChecker
import javax.inject.Inject

class AndroidPermissionChecker @Inject constructor(private val application: Application) :
    PermissionChecker {

    override fun check(permission: PermissionChecker.Permission): Boolean =
        ContextCompat.checkSelfPermission(
            application,
            permission.toAndroidId()
        ) == PackageManager.PERMISSION_GRANTED
}

private fun PermissionChecker.Permission.toAndroidId() = when (this) {
    PermissionChecker.Permission.FINE_LOCATION -> Manifest.permission.ACCESS_FINE_LOCATION
}
