package com.carterchen247.alarmscheduler.logger

interface AlarmSchedulerLogger {
    fun info(msg: String)
    fun error(msg: String, throwable: Throwable)

    companion object {
        val DEBUG = AlarmSchedulerDebugLogger()
    }
}