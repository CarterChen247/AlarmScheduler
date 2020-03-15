package com.carterchen247.alarmscheduler.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.carterchen247.alarmscheduler.RescheduleAlarmService


class RebootCompleteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        RescheduleAlarmService.startService(context)
    }
}