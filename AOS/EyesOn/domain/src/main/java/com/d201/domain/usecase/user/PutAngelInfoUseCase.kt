package com.d201.domain.usecase.user

import com.d201.domain.model.AngelInfo
import com.d201.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PutAngelInfoUseCase @Inject constructor(private val userRepository: UserRepository){
    fun execute(angelInfo: AngelInfo) = userRepository.putAngelInfo(angelInfo)
}