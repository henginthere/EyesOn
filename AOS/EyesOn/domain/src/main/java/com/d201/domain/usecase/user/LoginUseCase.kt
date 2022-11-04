package com.d201.domain.usecase.user


import com.d201.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    fun execute(idToken:String, fcmToken : String)
            = userRepository.loginUser(idToken, fcmToken)
}