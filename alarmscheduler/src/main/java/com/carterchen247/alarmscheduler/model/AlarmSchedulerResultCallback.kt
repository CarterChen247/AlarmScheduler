package com.carterchen247.alarmscheduler.model

interface AlarmSchedulerResultCallback<T> {
    fun onResult(result: T)
}