package com.d201.data.api

import com.d201.data.model.request.AngelRequest
import com.d201.data.model.request.UserRequest
import com.d201.data.model.request.UserRoleRequest
import com.d201.data.model.response.AngelInfoResponse
import com.d201.data.model.response.LoginResponse
import com.d201.domain.base.BaseResponse
import retrofit2.http.*

interface UserApi {

    @POST("user/login")
    suspend fun loginUser(@Body userRequest: UserRequest): BaseResponse<LoginResponse>

    @GET("user/info")
    suspend fun getAngelInfo(): BaseResponse<AngelInfoResponse>

    @POST("user/register")
    suspend fun putUserRole(@Body userRoleRequest: UserRoleRequest): BaseResponse<LoginResponse>

    @PUT("user/angel")
    suspend fun putAngelInfo(@Body angelRequest: AngelRequest): BaseResponse<AngelInfoResponse>

    @DELETE("user/info")
    suspend fun deleteUser(): BaseResponse<Void>


}