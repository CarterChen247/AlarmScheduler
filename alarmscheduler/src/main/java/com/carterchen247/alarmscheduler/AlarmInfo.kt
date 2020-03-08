package com.carterchen247.alarmscheduler

data class AlarmInfo(
    val alarmType: Int,
    val triggerAtMillis: Long,
    val alarmId: Int
)