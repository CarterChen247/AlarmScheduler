package com.carterchen247.alarmscheduler.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.carterchen247.alarmscheduler.logger.Logger
import com.carterchen247.alarmscheduler.service.RescheduleAlarmService


internal class RebootCompleteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Logger.d("RebootCompleteReceiver.onReceive() invoked")
        RescheduleAlarmService.startService(context)
    }
}