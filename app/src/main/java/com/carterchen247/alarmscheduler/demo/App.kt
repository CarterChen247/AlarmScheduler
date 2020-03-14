package com.carterchen247.alarmscheduler.demo

import android.app.Application
import com.carterchen247.alarmscheduler.AlarmScheduler
import com.carterchen247.alarmscheduler.AlarmTask
import com.carterchen247.alarmscheduler.AlarmTaskFactory
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        // init AlarmScheduler
        AlarmScheduler.init(this)
        AlarmScheduler.setAlarmTaskFactory(object : AlarmTaskFactory {
            override fun createAlarmTask(alarmType: Int): AlarmTask {
                return DemoAlarmTask()
            }
        })
        AlarmScheduler.setLogger(AlarmSchedulerLoggerImpl())
    }

}