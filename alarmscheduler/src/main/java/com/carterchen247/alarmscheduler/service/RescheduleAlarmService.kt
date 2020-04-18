package com.carterchen247.alarmscheduler.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.carterchen247.alarmscheduler.AlarmScheduler
import com.carterchen247.alarmscheduler.logger.Logger

internal class RescheduleAlarmService : JobIntentService() {

    companion object {
        private const val JOB_ID = 9453

        fun startService(context: Context) {
            enqueueWork(context, RescheduleAlarmService::class.java, JOB_ID, Intent())
        }
    }

    override fun onHandleWork(intent: Intent) {
        Logger.d("RescheduleAlarmService onHandleWork")
        delayReschedule()
        AlarmScheduler.getInstance().rescheduleAlarms()
    }

    /**
     * Since query and insert/remove operation may perform in different thread,
     * user may perform these operation as well when the application is launched,
     * add a slightly delay to avoid potential race condition, trying to guarantee
     * the data integrity.
     */
    private fun delayReschedule() {
        Thread.sleep(3000L)
    }
}