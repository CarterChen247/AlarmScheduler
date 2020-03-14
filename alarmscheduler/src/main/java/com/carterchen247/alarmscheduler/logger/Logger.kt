package com.carterchen247.alarmscheduler.logger

import android.util.Log

internal object Logger {

    private var logger: AlarmSchedulerLogger =
        NullLogger()
    var debugging = false

    fun setLogger(newLogger: AlarmSchedulerLogger?) {
        if (newLogger == null) {
            logger =
                NullLogger()
            debugging = false
        } else {
            logger = newLogger
            debugging = true
        }
    }

    fun d(msg: String) {
        if (debugging) {
            logger.log(Log.DEBUG, msg)
        }
    }

    fun e(msg: String) {
        if (debugging) {
            logger.log(Log.ERROR, msg)
        }
    }
}