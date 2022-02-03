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
        Logger.d("RescheduleAlarmService.onHandleWork() invoked")
        delayReschedule()
        AlarmScheduler.getImpl().rescheduleAlarms()
    }

    /**
     * since query and insert/remove operation may perform in different threads,
     * user may perform these operations as well when the application is launched,
     * add a slight delay to avoid potential race conditions, trying to guarantee
     * the data integrity
     */
    private fun delayReschedule() {
        Thread.sleep(3000L)
    }
}