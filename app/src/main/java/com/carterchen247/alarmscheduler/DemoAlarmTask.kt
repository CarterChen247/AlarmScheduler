package com.carterchen247.alarmscheduler

import timber.log.Timber

class DemoAlarmTask : AlarmTask {

    companion object {
        const val TYPE = 1
    }

    override fun onAlarmFires() {
        Timber.d("alarm fires")
    }
}
