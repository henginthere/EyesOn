package com.d201.domain.usecase

import com.d201.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAngelInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    fun excute() = userRepository.getAngelInfo()
}