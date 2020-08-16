package com.carterchen247.alarmscheduler

import android.content.Context
import com.carterchen247.alarmscheduler.logger.AlarmSchedulerLogger
import com.carterchen247.alarmscheduler.model.ScheduledAlarmsCallback
import com.carterchen247.alarmscheduler.task.AlarmTaskFactory
import java.util.concurrent.CountDownLatch

object AlarmScheduler : AlarmSchedulerContract {

    private var impl: AlarmSchedulerImpl? = null
    private val implInstanceLatch = CountDownLatch(1)

    @Synchronized
    fun init(context: Context) {
        impl = AlarmSchedulerImpl.getInstance(context)
        implInstanceLatch.countDown()
    }

    internal fun getImpl(): AlarmSchedulerImpl {
        implInstanceLatch.await()
        return requireNotNull(impl) { "AlarmSchedulerImpl is null, please call AlarmScheduler.init() first" }
    }

    override fun setAlarmTaskFactory(alarmTaskFactory: AlarmTaskFactory) {
        getImpl().setAlarmTaskFactory(alarmTaskFactory)
    }

    override fun setLogger(logger: AlarmSchedulerLogger?) {
        getImpl().setLogger(logger)
    }

    override fun isAlarmTaskScheduled(alarmId: Int): Boolean {
        return getImpl().isAlarmTaskScheduled(alarmId)
    }

    override fun cancelAlarmTask(alarmId: Int) {
        getImpl().cancelAlarmTask(alarmId)
    }

    override fun cancelAllAlarmTasks() {
        getImpl().cancelAllAlarmTasks()
    }

    override fun getScheduledAlarmsAsync(callback: ScheduledAlarmsCallback) {
        getImpl().getScheduledAlarmsAsync(callback)
    }
}

