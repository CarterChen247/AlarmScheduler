package com.carterchen247.alarmscheduler.error

interface AlarmSchedulerErrorHandler {
    fun handleError(error: Throwable)
}