package com.d201.domain.repository

import com.d201.domain.base.BaseResponse
import com.d201.domain.model.User
import com.d201.domain.utils.ResultType
import dagger.hilt.DefineComponent
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun loginUser(idToken:String, fcmToken:String): Flow<ResultType<BaseResponse<User>>>
}