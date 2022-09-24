package com.carterchen247.alarmscheduler.demo

import android.app.Application
import com.carterchen247.alarmscheduler.AlarmScheduler
import com.carterchen247.alarmscheduler.demo.log.EventBus
import com.carterchen247.alarmscheduler.error.AlarmSchedulerErrorHandler
import com.carterchen247.alarmscheduler.logger.AlarmSchedulerLogger
import com.carterchen247.alarmscheduler.task.AlarmTask
import com.carterchen247.alarmscheduler.task.AlarmTaskFactory
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        AlarmScheduler.setAlarmTaskFactory(object : AlarmTaskFactory {
            override fun createAlarmTask(alarmType: Int): AlarmTask {
                return DemoAlarmTask()
            }
        })
        AlarmScheduler.setLogger(AlarmSchedulerLogger.DEBUG)
        AlarmScheduler.setErrorHandler(object : AlarmSchedulerErrorHandler {
            override fun handleError(error: Throwable) {
                Timber.e(error)
                EventBus.dispatchMessage("error occurs, error=$error")
            }
        })
    }

}