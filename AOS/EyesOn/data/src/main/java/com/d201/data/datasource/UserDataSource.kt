package com.d201.data.datasource

import com.d201.data.api.UserApi
import com.d201.domain.base.BaseResponse
import com.d201.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataSource @Inject constructor(
    private val userApi: UserApi
){
    fun loginUser(idToken: String, fcmToken: String): Flow<Response<BaseResponse<UserEntity>>> = flow {
        emit(userApi.loginUser(idToken, fcmToken))
    }
}