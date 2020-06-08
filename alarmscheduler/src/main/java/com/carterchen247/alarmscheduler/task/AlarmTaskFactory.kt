package com.carterchen247.alarmscheduler.task

/**
 * use this class to create different type of alarm task according to your alarm type
 */
interface AlarmTaskFactory {
    fun createAlarmTask(alarmType: Int): AlarmTask
}