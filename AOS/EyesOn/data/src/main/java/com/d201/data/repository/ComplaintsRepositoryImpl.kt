package com.d201.data.repository

import com.d201.data.datasource.ComplaintsRemoteDataSource
import com.d201.data.mapper.mapperToComplaints
import com.d201.data.mapper.mapperToComplaintsRequest
import com.d201.domain.base.BaseResponse
import com.d201.domain.model.Complaints
import com.d201.domain.repository.ComplaintsRepository
import com.d201.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComplaintsRepositoryImpl @Inject constructor(private val complaintsRemoteDataSource: ComplaintsRemoteDataSource)
    : ComplaintsRepository {

    override fun insertComp(complaints: Complaints): Flow<ResultType<BaseResponse<Void>>> = flow {
        emit(ResultType.Loading)
        complaintsRemoteDataSource.insertComp(complaints.mapperToComplaintsRequest()).collect{
            emit(ResultType.Success(
                BaseResponse(
                    it.message,
                    it.status,
                    it.data
                )))
        }
    }

    override fun selectComplaintsBySeq(seq: Int): Flow<ResultType<BaseResponse<Complaints>>> = flow {
        emit(ResultType.Loading)
        complaintsRemoteDataSource.selectComplaintsBySeq(seq).collect{
            emit(ResultType.Success(
                BaseResponse(
                    it.message,
                    it.status,
                    it.data.mapperToComplaints()
                )
            ))
        }
    }

    override fun returnComplaints(complaints: Complaints): Flow<ResultType<BaseResponse<Void>>> = flow {
        emit(ResultType.Loading)
        complaintsRemoteDataSource.returnComplaints(complaints.mapperToComplaintsRequest()).collect{
            emit(ResultType.Success(
                BaseResponse(
                    it.message,
                    it.status,
                    it.data
                )))
        }
    }

    override fun submitComplaints(complaints: Complaints): Flow<ResultType<BaseResponse<Void>>> = flow {
        emit(ResultType.Loading)
        complaintsRemoteDataSource.submitComplaints(complaints.mapperToComplaintsRequest()).collect{
            emit(ResultType.Success(
                BaseResponse(
                    it.message,
                    it.status,
                    it.data
                )))
        }
    }

    override fun completeComplaints(complaints: Complaints): Flow<ResultType<BaseResponse<Void>>> = flow {
        emit(ResultType.Loading)
        complaintsRemoteDataSource.completeComplaints(complaints.mapperToComplaintsRequest()).collect{
            emit(ResultType.Success(
                BaseResponse(
                    it.message,
                    it.status,
                    it.data
                )))
        }
    }
}