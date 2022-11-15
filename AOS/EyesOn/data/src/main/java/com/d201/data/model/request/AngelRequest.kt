package com.d201.data.model.request

import com.google.gson.annotations.SerializedName

data class AngelRequest(
    @SerializedName("angelAlarmStart") val alarmStart: Int,
    @SerializedName("angelAlarmEnd") val alarmEnd: Int,
    @SerializedName("angelAlarmDay") val alarmDay: Int,
    @SerializedName("angelActive") val active: Boolean
)