package com.d201.domain.usecase.user

import com.d201.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PutUserRoleUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    fun execute(role: String, gender: String) = userRepository.putUserRole(role, gender)
}