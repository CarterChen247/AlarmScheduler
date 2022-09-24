package com.carterchen247.alarmscheduler

import android.annotation.SuppressLint
import com.carterchen247.alarmscheduler.error.AlarmSchedulerErrorHandler
import com.carterchen247.alarmscheduler.event.AlarmSchedulerEventObserver
import com.carterchen247.alarmscheduler.logger.AlarmSchedulerLogger
import com.carterchen247.alarmscheduler.model.AlarmConfig
import com.carterchen247.alarmscheduler.model.ScheduleResultCallback
import com.carterchen247.alarmscheduler.model.ScheduledAlarmsCallback
import com.carterchen247.alarmscheduler.task.AlarmTaskFactory

object AlarmScheduler : AlarmSchedulerContract {

    @SuppressLint("StaticFieldLeak")
    private val impl = ServiceLocator.provideAlarmSchedulerImpl()

    override fun setAlarmTaskFactory(alarmTaskFactory: AlarmTaskFactory) {
        impl.setAlarmTaskFactory(alarmTaskFactory)
    }

    override fun setLogger(loggerImpl: AlarmSchedulerLogger) {
        impl.setLogger(loggerImpl)
    }

    override fun setErrorHandler(errorHandler: AlarmSchedulerErrorHandler) {
        impl.setErrorHandler(errorHandler)
    }

    override fun isAlarmTaskScheduled(alarmId: Int): Boolean {
        return impl.isAlarmTaskScheduled(alarmId)
    }

    override fun cancelAlarmTask(alarmId: Int) {
        impl.cancelAlarmTask(alarmId)
    }

    override fun cancelAllAlarmTasks() {
        impl.cancelAllAlarmTasks()
    }

    override fun getScheduledAlarmsAsync(callback: ScheduledAlarmsCallback) {
        impl.getScheduledAlarmsAsync(callback)
    }

    override fun addEventObserver(observer: AlarmSchedulerEventObserver) {
        impl.addEventObserver(observer)
    }

    override fun removeEventObserver(observer: AlarmSchedulerEventObserver) {
        impl.removeEventObserver(observer)
    }

    override fun schedule(config: AlarmConfig, callback: ScheduleResultCallback?) {
        impl.schedule(config, callback)
    }

    override fun canScheduleExactAlarms() = impl.canScheduleExactAlarms()
}

