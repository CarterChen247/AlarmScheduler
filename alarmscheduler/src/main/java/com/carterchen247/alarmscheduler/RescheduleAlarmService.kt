package com.carterchen247.alarmscheduler

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.carterchen247.alarmscheduler.logger.Logger

class RescheduleAlarmService : JobIntentService() {

    companion object {
        private const val JOB_ID = 9453

        fun startService(context: Context) {
            enqueueWork(context, RescheduleAlarmService::class.java, JOB_ID, Intent())
        }
    }

    override fun onHandleWork(intent: Intent) {
        Logger.d("RescheduleAlarmService onHandleWork")
        AlarmScheduler.rescheduleAlarms()
    }
}