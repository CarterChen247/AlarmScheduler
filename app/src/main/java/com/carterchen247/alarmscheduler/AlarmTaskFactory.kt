package com.carterchen247.alarmscheduler

interface AlarmTaskFactory {
    fun createAlarmTask(alarmType: Int): AlarmTask
}