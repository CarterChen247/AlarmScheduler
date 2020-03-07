package com.carterchen247.alarmscheduler

import android.app.Application
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        // init AlarmScheduler
        AlarmScheduler.init(object : AlarmTaskFactory {
            override fun createAlarmTask(identifier: String): AlarmTask {
                return DemoAlarmTask()
            }
        })
    }
}