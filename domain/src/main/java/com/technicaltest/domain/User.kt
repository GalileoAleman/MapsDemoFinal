package com.technicaltest.domain

data class User (
    val email: String,
    val password: String,
    val loged : Boolean = false
)
