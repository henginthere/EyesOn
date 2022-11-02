package com.d201.data.model.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ResponseCompDto(
    @SerializedName("compSeq") val seq: Long,
    @SerializedName("compAddress") val address: String?,
    @SerializedName("compContent") val content: String?,
    @SerializedName("compImage") val image: String?,
    @SerializedName("compRegtime") val regTime: String?,
    @SerializedName("compResultContent") val resultContent: String?,
    @SerializedName("compReturn") val returnContent: String?,
    @SerializedName("compState") val state: String?,
    @SerializedName("compTitle") val title: String?
)