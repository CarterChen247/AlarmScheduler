package com.carterchen247.alarmscheduler.model

interface ScheduledAlarmsCallback {
    fun onResult(scheduledAlarms: List<AlarmInfo>)
}