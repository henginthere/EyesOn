package com.d201.domain.model

data class User(
    val accessToken : String,
    val refreshToken : String,
    val userSeq : Int,
    val userName : String,
    val userEmail : String,
    val status : Boolean
)