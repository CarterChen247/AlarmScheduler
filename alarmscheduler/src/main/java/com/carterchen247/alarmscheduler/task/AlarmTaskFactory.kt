package com.carterchen247.alarmscheduler.task

interface AlarmTaskFactory {
    fun createAlarmTask(alarmType: Int): AlarmTask
}