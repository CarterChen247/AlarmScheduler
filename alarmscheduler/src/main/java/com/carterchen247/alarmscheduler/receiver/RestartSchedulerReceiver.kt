package com.carterchen247.alarmscheduler.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

abstract class RestartSchedulerReceiver : BroadcastReceiver() {

    abstract fun restartScheduler()

    override fun onReceive(context: Context, intent: Intent) {
        // do nothing
    }

    companion object {
        const val ACTION = "com.carterchen247.alarmscheduler.receiver.RESTART_SCHEDULER"
    }
}