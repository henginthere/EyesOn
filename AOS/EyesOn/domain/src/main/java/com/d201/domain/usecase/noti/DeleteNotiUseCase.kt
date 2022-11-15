package com.d201.domain.usecase.noti

import com.d201.domain.model.Noti
import com.d201.domain.repository.NotiRepository
import javax.inject.Inject

class DeleteNotiUseCase @Inject constructor(
    private val notiRepository: NotiRepository
) {
    fun execute(noti: Noti) = notiRepository.deleteNoti(noti)
}