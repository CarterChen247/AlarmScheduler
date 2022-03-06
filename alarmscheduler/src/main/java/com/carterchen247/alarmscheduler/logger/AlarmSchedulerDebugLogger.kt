package com.carterchen247.alarmscheduler.logger

import android.util.Log

class AlarmSchedulerDebugLogger : AlarmSchedulerLogger {
    private val tag = "AlarmScheduler"

    override fun info(msg: String) {
        Log.i(tag, msg)
    }

    override fun error(msg: String, throwable: Throwable) {
        Log.e(tag, msg, throwable)
    }
}