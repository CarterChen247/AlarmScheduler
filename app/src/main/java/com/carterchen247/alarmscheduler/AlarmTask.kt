package com.carterchen247.alarmscheduler

import android.os.Bundle

interface AlarmTask {
    fun onAlarmFires(alarmInfo: AlarmInfo, customData: Bundle?)
}