package com.carterchen247.alarmscheduler.error

internal object ErrorHandler : AlarmSchedulerErrorHandler {
    var handler: AlarmSchedulerErrorHandler? = null

    override fun onError(error: Throwable) {
        handler?.onError(error)
    }
}