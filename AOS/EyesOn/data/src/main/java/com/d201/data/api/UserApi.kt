package com.d201.data.api

import com.d201.data.model.request.UserRequest
import com.d201.data.model.response.AngelInfoResponse
import com.d201.data.model.response.LoginResponse
import com.d201.domain.base.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {

    // 로그인
    @POST("user/login")
    suspend fun loginUser(@Body userRequest: UserRequest): BaseResponse<LoginResponse>

    @GET("user/info")
    suspend fun getAngelInfo(): BaseResponse<AngelInfoResponse>
}