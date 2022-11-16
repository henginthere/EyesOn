package com.d201.domain.repository

import com.d201.domain.base.BaseResponse
import com.d201.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow

interface HelpRepository {

    fun requestHelp(gender: String): Flow<ResultType<BaseResponse<Int>>>

    fun responseHelp(): Flow<ResultType<BaseResponse<Int>>>

    fun disconnectHelp(): Flow<ResultType<BaseResponse<Void>>>

}
