package com.carterchen247.alarmscheduler

interface AlarmTaskFactory {
    fun createAlarmTask(identifier: String): AlarmTask
}