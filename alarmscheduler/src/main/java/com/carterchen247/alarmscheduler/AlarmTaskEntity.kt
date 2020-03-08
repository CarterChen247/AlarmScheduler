package com.carterchen247.alarmscheduler

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlarmTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)