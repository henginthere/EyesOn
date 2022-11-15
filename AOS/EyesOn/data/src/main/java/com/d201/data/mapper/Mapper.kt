package com.d201.data.mapper

import com.d201.data.model.entity.NotiEntity
import com.d201.data.model.request.AngelRequest
import com.d201.data.model.request.ComplaintsRequest
import com.d201.data.model.response.AngelInfoResponse
import com.d201.data.model.response.ComplaintsResponse
import com.d201.data.model.response.LoginResponse
import com.d201.data.model.response.UserResponse
import com.d201.domain.model.*

fun UserResponse.mapperToUser(): User {
    return this.run {
        User(
            accessToken,
            refreshToken,
            userSeq,
            userName,
            userEmail,
            status
        )
    }
}

fun LoginResponse.mapperToToken(): Login {
    return this.run {
        Login(
            token,
            role,
            gender
        )
    }
}

fun AngelInfoResponse.mapperToAngelInfo(): AngelInfo {
    return this.run {
        AngelInfo(
            alarmStart,
            alarmEnd,
            alarmDay,
            compCnt,
            helpCnt,
            gender,
            active,
        )
    }
}

fun AngelInfo.mapperToAngelRequest(): AngelRequest {
    return this.run {
        AngelRequest(
            alarmStart,
            alarmEnd,
            alarmDay,
            active
        )
    }
}

fun Complaints.mapperToComplaintsRequest(): ComplaintsRequest {
    return this.run {
        ComplaintsRequest(
            seq,
            address,
            image,
            content,
            returnContent,
            title,
            resultContent
        )
    }
}

fun ComplaintsResponse.mapperToComplaints(): Complaints {
    return this.run {
        Complaints(
            seq,
            address,
            content,
            image,
            regTime,
            resultContent,
            returnContent,
            state,
            title
        )
    }
}

fun List<ComplaintsResponse>.mapperToListComplaints(): List<Complaints> {
    return this.toList().map {
        it.mapperToComplaints()
    }
}

fun List<NotiEntity>.mapperToNotis(): List<Noti> {
    return this.toList().map {
        Noti(
            it.seq,
            it.title,
            it.body,
            it.date
        )
    }
}

fun Noti.mapperToNotiEntity(): NotiEntity {
    return this.run {
        NotiEntity(
            seq,
            title,
            body,
            date
        )
    }
}