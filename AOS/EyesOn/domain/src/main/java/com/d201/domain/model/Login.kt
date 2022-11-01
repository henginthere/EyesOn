package com.d201.domain.model

data class Login(
    val jwtToken: JWTToken,
    val role: String,
    val gender: String
)