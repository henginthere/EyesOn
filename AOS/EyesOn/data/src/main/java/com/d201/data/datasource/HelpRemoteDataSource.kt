package com.d201.data.datasource

import com.d201.data.api.HelpApi
import com.d201.domain.base.BaseResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HelpRemoteDataSource @Inject constructor(
    private val helpApi: HelpApi
) {

    fun requestHelp(gender: String): Flow<BaseResponse<Int>> = flow {
        emit(helpApi.requestHelp(gender))
    }

    fun responseHelp(): Flow<BaseResponse<Int>> = flow {
        emit(helpApi.responseHelp())
    }

    fun disconnectHelp(): Flow<BaseResponse<Void>> = flow {
        emit(helpApi.disconnectHelp())
    }
}