package com.carterchen247.alarmscheduler

import timber.log.Timber

class DemoAlarmTask : AlarmTask {

    companion object {
        const val TAG = "DemoAlarmTask"
    }

    override fun onAlarmFires() {
        Timber.d("alarm fires")
    }
}
