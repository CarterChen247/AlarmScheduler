package com.carterchen247.alarmscheduler.event

sealed class AlarmSchedulerEvent

object ScheduleExactAlarmPermissionGrantedEvent : AlarmSchedulerEvent()