package com.d201.domain.base

import com.google.gson.annotations.SerializedName

data class BaseResponse<out T> (
    @SerializedName("message") val message : String,
    @SerializedName("status") val status : Int,
    @SerializedName("data") val data : T,
)