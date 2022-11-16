package com.d201.domain.usecase.complaints

import com.d201.domain.repository.ComplaintsRepository
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsertCompUseCase @Inject constructor(
    private val complaintsRepository: ComplaintsRepository
) {
    fun execute(complaints: MultipartBody.Part, imageFile: MultipartBody.Part) =
        complaintsRepository.insertComp(complaints, imageFile)
}