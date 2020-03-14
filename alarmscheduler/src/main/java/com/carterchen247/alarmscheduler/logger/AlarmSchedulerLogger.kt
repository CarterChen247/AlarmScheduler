package com.carterchen247.alarmscheduler.logger

interface AlarmSchedulerLogger {
    fun log(priority: Int, msg: String)
}