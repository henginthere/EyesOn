package com.d201.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Noti")
data class NotiEntity(
    @PrimaryKey(autoGenerate = true)
    var seq: Int = 0,
    val title: String,
    val body: String
)