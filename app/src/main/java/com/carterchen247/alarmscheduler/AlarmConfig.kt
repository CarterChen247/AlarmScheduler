package com.carterchen247.alarmscheduler

import android.os.Bundle

class AlarmConfig(
    private val alarmType: Int,
    private val triggerAtMillis: Long,
    private val alarmId: Int,
    private val data: Bundle? = null
) {
    fun getAlarmInfo() = AlarmInfo(
        alarmType,
        triggerAtMillis,
        alarmId
    )

    fun getData(): Bundle? {
        return data
    }
}