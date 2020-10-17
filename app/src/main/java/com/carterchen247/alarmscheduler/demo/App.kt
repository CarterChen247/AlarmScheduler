package com.carterchen247.alarmscheduler.demo

import android.app.Application
import com.carterchen247.alarmscheduler.AlarmScheduler
import com.carterchen247.alarmscheduler.demo.log.LogObservable
import com.carterchen247.alarmscheduler.error.AlarmSchedulerErrorHandler
import com.carterchen247.alarmscheduler.task.AlarmTask
import com.carterchen247.alarmscheduler.task.AlarmTaskFactory
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
        AlarmScheduler.setErrorHandler(object : AlarmSchedulerErrorHandler {
            override fun onError(error: Throwable) {
                Timber.e(error)
                LogObservable.dispatchMessage("error occurs, error=$error")
            }
        })
    }

}