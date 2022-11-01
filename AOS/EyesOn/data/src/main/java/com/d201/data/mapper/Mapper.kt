package com.d201.data.mapper

import com.d201.data.model.request.AngelRequest
import com.d201.data.model.request.ComplaintsRequest
import com.d201.data.model.response.AngelInfoResponse
import com.d201.data.model.response.ComplaintsResponse
import com.d201.data.model.response.LoginResponse
import com.d201.data.model.response.UserResponse
import com.d201.domain.model.AngelInfo
import com.d201.domain.model.Complaints
import com.d201.domain.model.Login
import com.d201.domain.model.User

fun UserResponse.mapperToUser(): User {
    return this.let {
        User(
            it.accessToken,
            it.refreshToken,
            it.userSeq,
            it.userName,
            it.userEmail,
            it.status
        )
    }
}

fun LoginResponse.mapperToToken(): Login{
    return this.let {
        Login(
            it.token,
            it.role,
            it.gender
        )
    }
}

fun AngelInfoResponse.mapperToAngelInfo(): AngelInfo{
    return this.let {
        AngelInfo(
            it.alarmStart,
            it.alarmEnd,
            it.alarmDay,
            it.compCnt,
            it.helpCnt,
            it.gender,
            it.active,
        )
    }
}

fun AngelInfo.mapperToAngelRequest(): AngelRequest{
    return this.let {
        AngelRequest(
            it.alarmStart,
            it.alarmEnd,
            it.alarmDay,
            it.active
        )
    }
}

fun Complaints.mapperToComplaintsRequest(): ComplaintsRequest{
    return this.let {
        ComplaintsRequest(
            it.address,
            it.image,
            it.content
        )
    }
}

fun ComplaintsResponse.mapperToComplaints(): Complaints{
    return Complaints(seq, blindUser, angelUser, state, returnContent, address, image, title, content, regTime, resultContent)
}