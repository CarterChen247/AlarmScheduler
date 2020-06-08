package com.carterchen247.alarmscheduler.demo

import com.carterchen247.alarmscheduler.demo.log.LogObservable
import com.carterchen247.alarmscheduler.logger.AlarmSchedulerLogger
import timber.log.Timber

class AlarmSchedulerLoggerImpl : AlarmSchedulerLogger {
    override fun log(priority: Int, msg: String) {
        Timber.log(priority, msg)
        LogObservable.dispatchMessage(msg)
    }
}