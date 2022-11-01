package com.d201.data.model.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ComplaintsResponse(
    @SerializedName("compSeq") val seq: Long,
    @SerializedName("blindUser") val blindUser: String,
    @SerializedName("angelUser") val angelUser: String,
    @SerializedName("compState") val state: String,
    @SerializedName("compReturn") val returnContent: String,
    @SerializedName("compAddress") val address: String,
    @SerializedName("compImage") val image: String,
    @SerializedName("compTitle") val title: String,
    @SerializedName("compContent") val content: String,
    @SerializedName("compRegTime") val regTime: LocalDateTime,
    @SerializedName("compResultContent") val resultContent: String
)