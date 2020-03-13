package com.carterchen247.alarmscheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

open class AddAlarmTaskFactoryReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("alarmScheduler", "AddAlarmTaskFactoryReceiver onReceive")
    }

    fun addAlarmTask() {
        Log.d("alarmScheduler", "AddAlarmTaskFactoryReceiver addAlarmTask")
    }
}