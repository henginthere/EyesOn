package com.d201.domain.usecase.complaints

import com.d201.domain.repository.ComplaintsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectCompBySeqUseCase @Inject constructor(
    private val complaintsRepository: ComplaintsRepository
) {
    fun execute(complaintsSeq: Long) = complaintsRepository.selectComplaintsBySeq(complaintsSeq)
}