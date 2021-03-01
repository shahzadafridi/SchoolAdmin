package com.empire.adminschool.Models

import androidx.annotation.Keep

@Keep
data class LoginResponse(
    val status: Int,
    val message: String,
    val token: String,
    val school: School,
    val user: User
)