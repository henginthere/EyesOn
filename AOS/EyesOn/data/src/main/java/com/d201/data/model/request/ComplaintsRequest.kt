package com.d201.data.model.request

import com.google.gson.annotations.SerializedName

data class ComplaintsRequest(
    @SerializedName("compAddress") val address: String,
    @SerializedName("compImage") val image: String,
    @SerializedName("compContent") val content: String,
)