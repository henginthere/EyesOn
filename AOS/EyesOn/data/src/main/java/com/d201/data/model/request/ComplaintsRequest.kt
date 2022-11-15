package com.d201.data.model.request

import com.google.gson.annotations.SerializedName

data class ComplaintsRequest(
    @SerializedName("compSeq") val seq: Long,
    @SerializedName("compAddress") val address: String?,
    @SerializedName("compImage") val image: String?,
    @SerializedName("compContent") val content: String?,
    @SerializedName("compReturn") val returnContent: String?,
    @SerializedName("compTitle") val title: String?,
    @SerializedName("compResultContent") val resultContent: String?
)