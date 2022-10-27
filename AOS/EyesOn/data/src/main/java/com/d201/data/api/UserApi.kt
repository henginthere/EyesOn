package com.d201.data.api

import com.d201.data.entity.UserEntity
import retrofit2.Response
import retrofit2.http.POST

interface UserApi {

    // 로그인
    @POST("api/user/check")
    suspend fun loginUser(idToken: String, fcmToken: String): Response<UserEntity>
}