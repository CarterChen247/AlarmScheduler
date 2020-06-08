package com.carterchen247.alarmscheduler

import com.carterchen247.alarmscheduler.logger.AlarmSchedulerLogger
import com.carterchen247.alarmscheduler.model.AlarmTaskCountCallback
import com.carterchen247.alarmscheduler.task.AlarmTaskFactory

/**
 * methods/functions which expose to the developer to interact with the library
 */
internal interface AlarmSchedulerContract {
    fun setAlarmTaskFactory(alarmTaskFactory: AlarmTaskFactory)
    fun setLogger(logger: AlarmSchedulerLogger?)
    fun isAlarmTaskScheduled(alarmId: Int): Boolean
    fun cancelAlarmTask(alarmId: Int)
    fun cancelAllAlarmTasks()
    fun getScheduledAlarmTaskCountAsync(callback: AlarmTaskCountCallback)
}