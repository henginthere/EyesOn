package com.d201.data.model.response

import com.google.gson.annotations.SerializedName

data class AngelInfoResponse(
    @SerializedName("angelAlarmStart") val alarmStart: Int,
    @SerializedName("angelAlarmEnd") val alarmEnd: Int,
    @SerializedName("angelAlarmDay") val alarmDay: Int,
    @SerializedName("angelCompCnt") val compCnt: Int,
    @SerializedName("angelHelpCnt") val helpCnt: Int,
    @SerializedName("angelGender") val gender: String,
    @SerializedName("angelActive") val active: Boolean

)