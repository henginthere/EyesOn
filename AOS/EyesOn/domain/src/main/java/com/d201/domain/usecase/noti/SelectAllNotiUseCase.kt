package com.d201.domain.usecase.noti

import com.d201.domain.repository.NotiRepository
import javax.inject.Inject

class SelectAllNotiUseCase @Inject constructor(
    private val notiRepository: NotiRepository
) {
    fun execute() = notiRepository.selectAllNotis()
}