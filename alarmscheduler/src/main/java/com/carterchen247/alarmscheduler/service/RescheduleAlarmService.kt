package com.carterchen247.alarmscheduler.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.carterchen247.alarmscheduler.ServiceLocator
import com.carterchen247.alarmscheduler.logger.LogMessage
import com.carterchen247.alarmscheduler.logger.Logger

internal class RescheduleAlarmService : JobIntentService() {

    private val alarmSchedulerImpl = ServiceLocator.provideAlarmSchedulerImpl()

    override fun onHandleWork(intent: Intent) {
        Logger.info(LogMessage.onRescheduleAlarmServiceOnHandleWork())
        delayReschedule()
        alarmSchedulerImpl.rescheduleAlarms()
    }

    /**
     * Since query and insert/remove operations may perform in different threads,
     * the user may perform these operations when the application is launched,
     * add a slight delay to avoid potential race conditions, and guarantee data integrity.
     */
    private fun delayReschedule() {
        Thread.sleep(3000L)
    }

    companion object {
        private const val JOB_ID = 9453
        fun startService(context: Context) {
            enqueueWork(context, RescheduleAlarmService::class.java, JOB_ID, Intent())
        }
    }
}