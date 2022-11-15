package com.d201.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.d201.data.datasource.ComplaintsRemoteDataSource
import com.d201.data.datasource.paging.ComplaintsPagingSource
import com.d201.data.mapper.mapperToComplaints
import com.d201.data.mapper.mapperToComplaintsRequest
import com.d201.domain.base.BaseResponse
import com.d201.domain.model.Complaints
import com.d201.domain.repository.ComplaintsRepository
import com.d201.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG ="ComplaintsRepositoryImpl"
@Singleton
class ComplaintsRepositoryImpl @Inject constructor(
    private val complaintsRemoteDataSource: ComplaintsRemoteDataSource
)
    : ComplaintsRepository {

    override fun insertComp(complaintsRequest: MultipartBody.Part, imageFile: MultipartBody.Part): Flow<ResultType<BaseResponse<Boolean>>> = flow {
        emit(ResultType.Loading)
        complaintsRemoteDataSource.insertComp(complaintsRequest, imageFile).collect{
            emit(ResultType.Success(
                BaseResponse(
                    it.message,
                    it.status,
                    it.data
                )))
        }
    }

    override fun selectComplaintsBySeq(seq: Long): Flow<ResultType<BaseResponse<Complaints>>> = flow {
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

    override fun selectComplaintsList(flag: Int) =
        Pager(
            config = PagingConfig(pageSize = 1, maxSize = 15, enablePlaceholders = false),
            pagingSourceFactory = { ComplaintsPagingSource(complaintsRemoteDataSource, flag) }
        ).flow

}