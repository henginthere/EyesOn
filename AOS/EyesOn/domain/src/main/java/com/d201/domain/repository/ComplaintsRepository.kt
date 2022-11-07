package com.d201.domain.repository

import androidx.paging.PagingData
import com.d201.domain.base.BaseResponse
import com.d201.domain.model.Complaints
import com.d201.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ComplaintsRepository {
    fun insertComp(complaintsRequest: MultipartBody.Part, imageFile: MultipartBody.Part): Flow<ResultType<BaseResponse<Void>>>

    fun selectComplaintsBySeq(seq: Long): Flow<ResultType<BaseResponse<Complaints>>>

    fun returnComplaints(complaintsRequest: Complaints): Flow<ResultType<BaseResponse<Void>>>

    fun submitComplaints(complaintsRequest: Complaints): Flow<ResultType<BaseResponse<Void>>>

    fun completeComplaints(complaintsRequest: Complaints): Flow<ResultType<BaseResponse<Void>>>

    fun selectAllComplaints(flag: Int): Flow<PagingData<Complaints>>

}