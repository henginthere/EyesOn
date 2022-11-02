package com.d201.data.model.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ComplaintsResponse(
    @SerializedName("compSeq") val seq: Long,
    @SerializedName("compAddress") val address: String,
    @SerializedName("compContent") val content: String,
    @SerializedName("compImage") val image: String,
    @SerializedName("compRegTime") val regTime: LocalDateTime,
    @SerializedName("compResultContent") val resultContent: String,
    @SerializedName("compReturn") val returnContent: String,
    @SerializedName("compState") val state: Int,
    @SerializedName("compTitle") val title: String
)