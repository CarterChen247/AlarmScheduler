package com.carterchen247.alarmscheduler.extension

import android.app.AlarmManager
import android.os.Build

fun AlarmManager.canScheduleExactAlarmsCompat() = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> canScheduleExactAlarms()
    else -> true
}