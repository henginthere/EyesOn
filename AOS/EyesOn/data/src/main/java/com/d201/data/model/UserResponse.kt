package com.d201.data.model

data class UserResponse(
    val accessToken : String,
    val refreshToken : String,
    val userSeq : Int,
    val userName : String,
    val userEmail : String,
    val status : Boolean
)