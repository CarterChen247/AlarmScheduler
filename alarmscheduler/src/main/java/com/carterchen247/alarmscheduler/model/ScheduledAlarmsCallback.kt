package com.carterchen247.alarmscheduler.model

fun interface ScheduledAlarmsCallback {
    fun onResult(scheduledAlarms: List<AlarmInfo>)
}