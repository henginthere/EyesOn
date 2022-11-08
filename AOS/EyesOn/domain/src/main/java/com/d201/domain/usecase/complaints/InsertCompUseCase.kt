package com.d201.domain.usecase.complaints

import com.d201.domain.model.Complaints
import com.d201.domain.repository.ComplaintsRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsertCompUseCase @Inject constructor(private val complaintsRepository: ComplaintsRepository) {
    fun excute(complaints: MultipartBody.Part, imageFile: MultipartBody.Part) = complaintsRepository.insertComp(complaints, imageFile)
}