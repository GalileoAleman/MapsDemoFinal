package com.technicaltest.data

interface PermissionChecker {

    enum class Permission { FINE_LOCATION }

    fun check(permission: Permission): Boolean
}
