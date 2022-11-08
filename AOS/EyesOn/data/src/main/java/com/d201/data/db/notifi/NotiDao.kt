package com.d201.data.db.notifi

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.d201.data.model.entity.NotiEntity

@Dao
interface NotiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNoti(noti: NotiEntity)

    @Query("SELECT * FROM Noti")
    fun selectAllNotis() : List<NotiEntity>
}