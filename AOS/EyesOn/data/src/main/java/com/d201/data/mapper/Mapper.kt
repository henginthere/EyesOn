package com.d201.data.mapper

import com.d201.data.model.response.TokenResponse
import com.d201.data.model.response.UserResponse
import com.d201.domain.model.Token
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

fun TokenResponse.mapperToToken(): Token{
    return this.let {
        Token(
            it.accessToken,
            it.refreshToken
        )
    }
}