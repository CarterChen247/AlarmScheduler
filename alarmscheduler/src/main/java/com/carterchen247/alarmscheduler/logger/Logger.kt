package com.carterchen247.alarmscheduler.logger

import android.util.Log

internal object Logger {

    var logger: AlarmSchedulerLogger? = null

    fun d(msg: String) {
        logger?.log(Log.DEBUG, msg)
    }

    fun e(msg: String) {
        logger?.log(Log.ERROR, msg)
    }
}