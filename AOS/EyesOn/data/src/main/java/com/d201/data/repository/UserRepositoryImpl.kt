package com.d201.data.repository

import com.d201.domain.dto.User
import com.d201.domain.repository.UserRepository
import com.d201.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(

): UserRepository {
    override fun loginUser(idToken: String, fcmToken: String): Flow<ResultType<User>> {
        TODO("Not yet implemented")
    }


}