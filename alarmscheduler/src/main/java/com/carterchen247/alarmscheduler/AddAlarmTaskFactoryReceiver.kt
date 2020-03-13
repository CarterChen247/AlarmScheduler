package com.carterchen247.alarmscheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

class AddAlarmTaskFactoryReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Timber.d("AddAlarmTaskFactoryReceiver onReceive")
    }

    fun addAlarmTask() {
        Timber.d("AddAlarmTaskFactoryReceiver addAlarmTask")
    }
}