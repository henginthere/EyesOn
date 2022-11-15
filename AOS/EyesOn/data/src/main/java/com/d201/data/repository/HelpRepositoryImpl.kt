package com.d201.data.repository

import android.util.Log
import com.d201.data.datasource.HelpRemoteDataSource
import com.d201.domain.base.BaseResponse
import com.d201.domain.repository.HelpRepository
import com.d201.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "HelpRepositoryImpl"
@Singleton
class HelpRepositoryImpl @Inject constructor(
    private val helpRemoteDataSource: HelpRemoteDataSource
) : HelpRepository {
    override fun requestHelp(gender: String): Flow<ResultType<BaseResponse<Int>>> = flow {
        emit(ResultType.Loading)
        helpRemoteDataSource.requestHelp(gender).collect {
            when (it.status) {
                200 -> emit(ResultType.Success(it))
                else -> emit(ResultType.InputError)
            }
        }
    }

    override fun responseHelp(): Flow<ResultType<BaseResponse<Int>>> = flow {
        emit(ResultType.Loading)
        helpRemoteDataSource.responseHelp().collect {
            when (it.status) {
                200 -> emit(ResultType.Success(it))
            }
        }
    }

    override fun disconnectHelp(): Flow<ResultType<BaseResponse<Void>>> = flow {
        emit(ResultType.Loading)
        helpRemoteDataSource.disconnectHelp().collect {
            when (it.status) {
                200 -> ResultType.Success(it)
            }
        }
    }

}