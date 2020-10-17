package com.carterchen247.alarmscheduler.error

interface AlarmSchedulerErrorHandler {
    fun onError(error: Throwable)
}