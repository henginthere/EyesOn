package com.d201.data.repository

import com.d201.data.datasource.NotiLocalDataSource
import com.d201.data.mapper.mapperToNotiEntity
import com.d201.data.mapper.mapperToNotis
import com.d201.domain.model.Noti
import com.d201.domain.repository.NotiRepository
import com.d201.domain.utils.ResultType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotiRepositoryImpl @Inject constructor(
    private val notiLocalDataSource: NotiLocalDataSource
) : NotiRepository {
    override fun insertNoti(noti: Noti) {
        notiLocalDataSource.insertNoti(noti.mapperToNotiEntity())
    }

    override fun selectAllNotis(): Flow<ResultType<List<Noti>>> = flow {
        emit(ResultType.Loading)
        notiLocalDataSource.selectAllNoti().collect{
            when(it.size){
                0 -> emit(ResultType.Empty)
                else -> emit(ResultType.Success(it.mapperToNotis()))
            }
        }
    }
}