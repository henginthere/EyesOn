package com.d201.data.datasource

import com.d201.data.db.notifi.NotiDao
import com.d201.data.model.entity.NotiEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotiLocalDataSource @Inject constructor(
    private val notiDao: NotiDao
) {
    fun insertNoti(notiEntity: NotiEntity) = notiDao.insertNoti(notiEntity)

    fun selectAllNoti() : Flow<List<NotiEntity>> = flow {
        emit(notiDao.selectAllNotis())
    }
}