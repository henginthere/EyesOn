package com.d201.domain.repository

import androidx.paging.PagingData
import com.d201.domain.base.BaseResponse
import com.d201.domain.model.Complaints
import com.d201.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface ComplaintsRepository {
    
    fun insertComp(
        complaintsRequest: MultipartBody.Part,
        imageFile: MultipartBody.Part
    ): Flow<ResultType<BaseResponse<Boolean>>>

    fun selectComplaintsBySeq(seq: Long): Flow<ResultType<BaseResponse<Complaints>>>

    fun returnComplaints(complaintsRequest: Complaints): Flow<ResultType<BaseResponse<Void>>>

    fun submitComplaints(complaintsRequest: Complaints): Flow<ResultType<BaseResponse<Void>>>

    fun completeComplaints(complaintsRequest: Complaints): Flow<ResultType<BaseResponse<Void>>>

    fun selectComplaintsList(flag: Int): Flow<PagingData<Complaints>>

}