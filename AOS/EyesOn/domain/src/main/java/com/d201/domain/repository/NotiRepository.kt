package com.d201.domain.repository

import com.d201.domain.model.Noti
import com.d201.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow

interface NotiRepository {

    fun insertNoti(noti: Noti)

    fun selectAllNotis(): Flow<ResultType<List<Noti>>>

    fun deleteNoti(noti: Noti)

}