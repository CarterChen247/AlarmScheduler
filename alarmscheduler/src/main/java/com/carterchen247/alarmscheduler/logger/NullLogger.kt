package com.carterchen247.alarmscheduler.logger

internal class NullLogger : AlarmSchedulerLogger {
    override fun log(priority: Int, msg: String) {
        // do nothing
    }
}