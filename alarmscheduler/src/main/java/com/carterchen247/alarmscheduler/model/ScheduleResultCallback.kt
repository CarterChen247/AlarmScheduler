package com.carterchen247.alarmscheduler.model

fun interface ScheduleResultCallback {
    fun onResult(result: ScheduleResult)
}