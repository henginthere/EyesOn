package com.d201.domain.usecase.complaints

import com.d201.domain.repository.ComplaintsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectAllCompUseCase @Inject constructor(private val complaintsRepository: ComplaintsRepository) {
    fun excute(flag: Int) = complaintsRepository.selectAllComplaints(flag)
}