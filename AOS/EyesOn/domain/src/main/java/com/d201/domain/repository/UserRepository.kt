package com.d201.domain.repository

import com.d201.domain.dto.User
import com.d201.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow


interface UserRepository {

    fun loginUser(idToken:String, fcmToken:String): Flow<ResultType<User>>
}