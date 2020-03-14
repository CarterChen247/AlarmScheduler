package com.carterchen247.alarmscheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

open class RestartSchedulerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("alarmScheduler", "AddAlarmTaskFactoryReceiver onReceive")
    }

    fun restartScheduler() {
        Log.d("alarmScheduler", "AddAlarmTaskFactoryReceiver addAlarmTask")
    }
}