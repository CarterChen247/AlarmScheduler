package com.carterchen247.alarmscheduler.event

fun interface AlarmSchedulerEventObserver {
    fun onEventDispatched(event: AlarmSchedulerEvent)
}