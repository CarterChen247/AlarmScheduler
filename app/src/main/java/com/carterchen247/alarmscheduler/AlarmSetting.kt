package com.carterchen247.alarmscheduler

data class AlarmSetting(
    val identifier: String,
    val triggerAtMillis: Long
)