package com.carterchen247.alarmscheduler

object AlarmScheduler {

    private var alarmTaskFactory: AlarmTaskFactory? = null

    fun init(factory: AlarmTaskFactory) {
        alarmTaskFactory = factory
    }

    fun schedule(setting: AlarmSetting) {
        // TODO()
    }
}