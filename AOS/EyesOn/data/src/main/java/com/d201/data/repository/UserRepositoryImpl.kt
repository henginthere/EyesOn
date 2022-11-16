package com.d201.data.repository

import com.d201.data.datasource.UserRemoteDataSource
import com.d201.data.mapper.mapperToAngelInfo
import com.d201.data.mapper.mapperToAngelRequest
import com.d201.data.mapper.mapperToToken
import com.d201.data.model.request.UserRequest
import com.d201.data.model.request.UserRoleRequest
import com.d201.domain.base.BaseResponse
import com.d201.domain.model.AngelInfo
import com.d201.domain.model.Login
import com.d201.domain.repository.UserRepository
import com.d201.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "UserRepositoryImpl"

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {

    override fun loginUser(
        idToken: String,
        fcmToken: String
    ): Flow<ResultType<BaseResponse<Login>>> = flow {
        emit(ResultType.Loading)
        userRemoteDataSource.loginUser(UserRequest(idToken, fcmToken)).collect {
            emit(
                ResultType.Success(
                    BaseResponse(
                        it.message,
                        it.status,
                        it.data.mapperToToken()
                    )
                )
            )
        }
    }

    override fun getAngelInfo(): Flow<ResultType<BaseResponse<AngelInfo>>> = flow {
        emit(ResultType.Loading)
        userRemoteDataSource.getAngelInfo().collect {
            emit(
                ResultType.Success(
                    BaseResponse(
                        it.message,
                        it.status,
                        it.data.mapperToAngelInfo()
                    )
                )
            )
        }
    }

    override fun putUserRole(role: String, gender: String): Flow<ResultType<BaseResponse<Login>>> =
        flow {
            emit(ResultType.Loading)
            userRemoteDataSource.putUserRole(UserRoleRequest(gender, role)).collect {
                emit(
                    ResultType.Success(
                        BaseResponse(
                            it.message,
                            it.status,
                            it.data.mapperToToken()
                        )
                    )
                )
            }
        }

    override fun putAngelInfo(angelInfo: AngelInfo): Flow<ResultType<BaseResponse<AngelInfo>>> =
        flow {
            emit(ResultType.Loading)
            userRemoteDataSource.putAngelInfo(angelInfo.mapperToAngelRequest()).collect {
                emit(
                    ResultType.Success(
                        BaseResponse(
                            it.message,
                            it.status,
                            it.data.mapperToAngelInfo()
                        )
                    )
                )
            }
        }

    override fun deleteUser(): Flow<ResultType<BaseResponse<Void>>> = flow {
        emit(ResultType.Loading)
        userRemoteDataSource.deleteUser().collect() {
            emit(
                ResultType.Success(
                    BaseResponse(
                        it.message,
                        it.status,
                        it.data
                    )
                )
            )
        }
    }
}