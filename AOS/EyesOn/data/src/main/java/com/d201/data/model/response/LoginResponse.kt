package com.d201.data.model.response

import com.d201.domain.model.JWTToken
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("tokenDto") val token: JWTToken,
    @SerializedName("role") val role: String,
    @SerializedName("gender") val gender: String
)