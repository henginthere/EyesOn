package com.d201.domain.usecase.complaints

import com.d201.domain.repository.ComplaintsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectCompByBlindUseCase @Inject constructor(
    private val complaintsRepository: ComplaintsRepository
) {
    fun execute() = complaintsRepository.selectComplaintsList(2)
}