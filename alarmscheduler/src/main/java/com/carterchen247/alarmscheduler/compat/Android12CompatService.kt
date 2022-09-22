package com.carterchen247.alarmscheduler.compat

import android.app.AlarmManager
import android.os.Build

class Android12CompatService(
    private val alarmManager: AlarmManager
) : CompatService {

    override fun canScheduleExactAlarmsCompat() = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> alarmManager.canScheduleExactAlarms()
        else -> true
    }
}