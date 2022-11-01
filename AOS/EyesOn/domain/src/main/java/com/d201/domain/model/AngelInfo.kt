package com.d201.domain.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class AngelInfo(
    val alarmStart: Int,
    val alarmEnd: Int,
    val alarmDay: Int,
    val compCnt: Int,
    val helpCnt: Int,
    val gender: String,
    val active: Boolean
)