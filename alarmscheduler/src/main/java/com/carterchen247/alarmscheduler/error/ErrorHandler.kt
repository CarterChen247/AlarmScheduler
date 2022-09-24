package com.carterchen247.alarmscheduler.error

internal object ErrorHandler : AlarmSchedulerErrorHandler {
    var handler: AlarmSchedulerErrorHandler? = null

    override fun handleError(error: Throwable) {
        handler?.handleError(error)
    }
}