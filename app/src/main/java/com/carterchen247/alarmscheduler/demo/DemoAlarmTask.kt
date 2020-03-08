package com.carterchen247.alarmscheduler.demo

import android.os.Bundle
import com.carterchen247.alarmscheduler.AlarmTask
import timber.log.Timber

class DemoAlarmTask : AlarmTask {

    companion object {
        const val TYPE = 1
    }

    override fun onAlarmFires(alarmId: Int, customData: Bundle?) {
        Timber.d("alarm fires")
    }
}
