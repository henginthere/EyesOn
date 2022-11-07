package com.d201.data.api

import com.d201.data.model.request.ComplaintsRequest
import com.d201.data.model.response.ComplaintsResponse
import com.d201.domain.base.BaseResponse
import com.d201.domain.model.PagingResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ComplaintsApi {

    @Multipart
    @POST("complaints/register")
    suspend fun insertComp(@Part complaintsRequest: MultipartBody.Part, @Part imageFile: MultipartBody.Part): BaseResponse<Void>

    @GET("complaints/list")
    suspend fun selectAllComplaints(@Query("page") page: Int, @Query("size") size: Int): BaseResponse<PagingResult<ComplaintsResponse>>

    @GET("complaints/list/angel")
    suspend fun selectComplaintsByAngel(@Query("page") page: Int, @Query("size") size: Int): BaseResponse<PagingResult<ComplaintsResponse>>

    @GET("complaints/list/blind")
    suspend fun selectComplaintsByBlind(@Query("page") page: Int, @Query("size") size: Int): BaseResponse<PagingResult<ComplaintsResponse>>

    @GET("complaints/{complaintsSeq}")
    suspend fun selectComplaintsBySeq(@Path("complaintsSeq") seq: Long): BaseResponse<ComplaintsResponse>

    @PUT("complaints/return")
    suspend fun returnComplaints(@Body complaintsRequest: ComplaintsRequest): BaseResponse<Void>

    @PUT("complaints/submit")
    suspend fun submitComplaints(@Body complaintsRequest: ComplaintsRequest): BaseResponse<Void>

    @PUT("complaints/complete")
    suspend fun completeComplaints(@Body complaintsRequest: ComplaintsRequest): BaseResponse<Void>
}