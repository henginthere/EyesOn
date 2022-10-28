package com.d201.data.repository

import com.d201.domain.base.BaseResponse
import com.d201.data.datasource.UserDataSource
import com.d201.data.entity.UserEntity
import com.d201.domain.repository.UserRepository
import com.d201.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
): UserRepository {
    override fun loginUser(idToken: String, fcmToken: String): Flow<ResultType<Response<BaseResponse<UserEntity>>>> = flow {
        emit(ResultType.Loading)
        userDataSource.loginUser(idToken, fcmToken).collect {
            emit(ResultType.Success(it))
        }
    }


}