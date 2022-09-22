package com.carterchen247.alarmscheduler.storage

import com.carterchen247.alarmscheduler.model.AlarmInfo

interface AlarmStateDataSource {
    suspend fun addAlarm(alarmInfo: AlarmInfo): Long
    suspend fun getAlarms(): List<AlarmInfo>
    suspend fun removeAlarm(alarmId: Int)
}