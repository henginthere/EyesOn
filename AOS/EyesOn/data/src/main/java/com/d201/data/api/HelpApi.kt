package com.d201.data.api

import com.d201.domain.base.BaseResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface HelpApi {

    @POST("help/{gender}")
    suspend fun requestHelp(@Path("gender") gender: String): BaseResponse<Int>

    @GET("help")
    suspend fun responseHelp(): BaseResponse<Int>

    @PUT("help/finish")
    suspend fun disconnectHelp(): BaseResponse<Void>
}