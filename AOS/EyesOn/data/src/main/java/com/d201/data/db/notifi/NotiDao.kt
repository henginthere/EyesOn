package com.d201.data.db.notifi

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.d201.data.model.entity.NotiEntity
import com.d201.domain.model.Noti

@Dao
interface NotiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNoti(notiEntity: NotiEntity)

    @Query("SELECT * FROM Noti")
    fun selectAllNotis() : List<NotiEntity>

    @Delete
    fun deleteNoti(notiEntity: NotiEntity)
}