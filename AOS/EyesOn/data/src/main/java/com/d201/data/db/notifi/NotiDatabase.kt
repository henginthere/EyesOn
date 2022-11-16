package com.d201.data.db.notifi

import androidx.room.Database
import androidx.room.RoomDatabase
import com.d201.data.model.entity.NotiEntity

@Database(entities = [NotiEntity::class], version = 2, exportSchema = false)
abstract class EyesOnDatabase : RoomDatabase() {

    abstract fun notiDao(): NotiDao

}