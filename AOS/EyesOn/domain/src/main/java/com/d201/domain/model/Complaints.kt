package com.d201.domain.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Complaints(
    val seq: Long,
    val blindUser: String,
    val angelUser: String,
    val state: String,
    val returnContent: String,
    val address: String,
    val image: String,
    val title: String,
    val content: String,
    val regTime: LocalDateTime,
    val resultContent: String
)