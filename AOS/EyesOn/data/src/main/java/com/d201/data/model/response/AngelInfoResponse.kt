package com.d201.data.model.response

import com.d201.domain.model.JWTToken
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class AngelInfoResponse(
    @SerializedName("angelAlarmStart") val alarmStart: LocalDateTime,
    @SerializedName("angelAlarmEnd") val alarmEnd: LocalDateTime,
    @SerializedName("angelAlarmDay") val alarmDay: Int,
    @SerializedName("angelCompCnt") val compCnt: Int,
    @SerializedName("angelHelpCnt") val helpCnt: Int,
    @SerializedName("angelGender") val gender: String,
    @SerializedName("angelActive") val active: Boolean

)