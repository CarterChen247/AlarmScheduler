package com.carterchen247.alarmscheduler

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
@TypeConverters(DataPayloadTypeConverter::class)
data class AlarmTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dataPayload: DataPayload? = null
)