package com.d201.domain.model

data class AngelInfo(
    val alarmStart: Int,
    val alarmEnd: Int,
    val alarmDay: Int,
    val compCnt: Int,
    val helpCnt: Int,
    val gender: String,
    val active: Boolean
){
    constructor(alarmStart: Int, alarmEnd: Int, alarmDay: Int, active: Boolean):
            this(alarmStart, alarmEnd, alarmDay, 0, 0, "",active)
}