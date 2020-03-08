package com.carterchen247.alarmscheduler

import android.os.Bundle

interface AlarmTask {
    fun onAlarmFires(alarmId: Int, customData: Bundle?)
}