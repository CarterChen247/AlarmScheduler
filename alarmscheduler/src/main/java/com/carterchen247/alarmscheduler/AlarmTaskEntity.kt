package com.carterchen247.alarmscheduler

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
@TypeConverters(DataPayloadTypeConverter::class)
data class AlarmTaskEntity(
    val type: Int = -1,
    val triggerTime: Long = -1,
    val dataPayload: DataPayload? = null,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)