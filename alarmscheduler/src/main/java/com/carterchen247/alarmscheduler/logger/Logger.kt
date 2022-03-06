package com.carterchen247.alarmscheduler.logger

/**
 * The proxy of the real [AlarmSchedulerLogger] implementation.
 */
internal object Logger : AlarmSchedulerLogger {
    private var logger: AlarmSchedulerLogger? = null

    override fun info(msg: String) {
        logger?.info(msg)
    }

    override fun error(msg: String, throwable: Throwable) {
        logger?.error(msg, throwable)
    }

    fun setDelegate(logger: AlarmSchedulerLogger) {
        this.logger = logger
    }
}