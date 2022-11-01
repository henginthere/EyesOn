package com.d201.domain.usecase

import com.d201.domain.model.Login
import com.d201.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PutUserRoleUseCase @Inject constructor(private val userRepository: UserRepository) {
    fun excute(role: String, gender: String) = userRepository.putUserRole(role, gender)
}