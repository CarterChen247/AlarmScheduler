package com.carterchen247.alarmscheduler.logger

/**
 * implement this interface to monitor the behavior of the library
 */
interface AlarmSchedulerLogger {
    fun log(priority: Int, msg: String)
}