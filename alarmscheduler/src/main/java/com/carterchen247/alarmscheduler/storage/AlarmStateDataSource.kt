package com.carterchen247.alarmscheduler.storage

import com.carterchen247.alarmscheduler.model.AlarmInfo

interface AlarmStateDataSource {
    suspend fun add(alarmInfo: AlarmInfo): Long
    suspend fun getAll(): List<AlarmInfo>
    suspend fun removeImmediately(id: Int)
}