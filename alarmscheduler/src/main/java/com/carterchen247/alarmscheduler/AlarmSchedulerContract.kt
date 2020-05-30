package com.carterchen247.alarmscheduler

import com.carterchen247.alarmscheduler.logger.AlarmSchedulerLogger
import com.carterchen247.alarmscheduler.task.AlarmTaskFactory

internal interface AlarmSchedulerContract {
    fun setAlarmTaskFactory(alarmTaskFactory: AlarmTaskFactory)
    fun setLogger(logger: AlarmSchedulerLogger?)
    fun cancelAlarmTask(alarmId: Int)
    fun isAlarmTaskRunning(alarmId: Int): Boolean
    fun cancelAllAlarmTasks()
}