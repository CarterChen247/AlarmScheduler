package com.carterchen247.alarmscheduler

import android.os.Bundle
import timber.log.Timber

class DemoAlarmTask : AlarmTask {

    companion object {
        const val TYPE = 1
    }

    override fun onAlarmFires(alarmInfo: AlarmInfo, customData: Bundle?) {
        Timber.d("alarm fires")
    }
}
