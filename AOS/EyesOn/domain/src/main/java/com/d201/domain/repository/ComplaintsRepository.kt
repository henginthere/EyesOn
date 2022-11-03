package com.d201.domain.repository

import androidx.paging.PagingData
import com.d201.domain.base.BaseResponse
import com.d201.domain.model.Complaints
import com.d201.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow

interface ComplaintsRepository {
    fun insertComp(complaintsRequest: Complaints): Flow<ResultType<BaseResponse<Void>>>

    fun selectComplaintsBySeq(seq: Int): Flow<ResultType<BaseResponse<Complaints>>>

    fun returnComplaints(complaintsRequest: Complaints): Flow<ResultType<BaseResponse<Void>>>

    fun submitComplaints(complaintsRequest: Complaints): Flow<ResultType<BaseResponse<Void>>>

    fun completeComplaints(complaintsRequest: Complaints): Flow<ResultType<BaseResponse<Void>>>

    fun selectAllComplaints(): Flow<PagingData<Complaints>>

    fun selectComplaintsByAngel(flag: Int): Flow<ResultType<PagingData<Complaints>>>

    fun selectComplaintsByBlind(flag: Int): Flow<ResultType<PagingData<Complaints>>>

}