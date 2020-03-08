package com.carterchen247.alarmscheduler

import android.os.Bundle

class AlarmConfig(
    private val alarmType: Int,
    private val triggerAtMillis: Long
) {
    var alarmId: Int = 0
    var customData: Bundle? = null

    fun getAlarmInfo() = AlarmInfo(
        alarmType,
        triggerAtMillis,
        alarmId
    )
}