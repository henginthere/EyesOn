package com.d201.data.db.notifi

import androidx.room.*
import com.d201.data.model.entity.NotiEntity

@Dao
interface NotiDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNoti(notiEntity: NotiEntity)

    @Query("SELECT * FROM Noti")
    fun selectAllNotis(): List<NotiEntity>

    @Delete
    fun deleteNoti(notiEntity: NotiEntity)

}