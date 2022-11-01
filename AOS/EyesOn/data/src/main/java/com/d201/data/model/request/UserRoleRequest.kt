package com.d201.data.model.request

import com.google.gson.annotations.SerializedName

data class UserRoleRequest(
    @SerializedName("userGender") val gender: String,
    @SerializedName("userRole") val role: String,
)