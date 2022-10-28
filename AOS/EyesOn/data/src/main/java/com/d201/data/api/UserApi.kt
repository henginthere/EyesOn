package com.d201.data.api

import com.d201.domain.base.BaseResponse
import com.d201.data.model.UserResponse
import retrofit2.http.POST

interface UserApi {

    // 로그인
    @POST("api/user/check")
    suspend fun loginUser(idToken: String, fcmToken: String): BaseResponse<UserResponse>

}