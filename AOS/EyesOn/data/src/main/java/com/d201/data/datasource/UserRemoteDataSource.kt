package com.d201.data.datasource

import com.d201.data.api.UserApi
import com.d201.domain.base.BaseResponse
import com.d201.data.model.UserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSource @Inject constructor(
    private val userApi: UserApi
){
    fun loginUser(idToken: String, fcmToken: String): Flow<BaseResponse<UserResponse>> = flow {
        emit(userApi.loginUser(idToken, fcmToken))
    }
}