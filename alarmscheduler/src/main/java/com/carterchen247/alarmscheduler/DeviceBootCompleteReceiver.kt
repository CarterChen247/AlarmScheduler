package com.carterchen247.alarmscheduler

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

class DeviceBootCompleteReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        rescheduleAlarms(context)
    }

    private fun rescheduleAlarms(context: Context) {
        val d = AlarmTaskDatabase.getInstance(context)
            .getAlarmTaskDao()
            .selectAll()
            .subscribe({ alarmTasks ->
                alarmTasks.forEach {
                    AlarmScheduler.schedule(
                        AlarmConfig(
                            it.type,
                            it.triggerTime
                        ).apply {
                            alarmId = it.id
                            dataPayload = it.dataPayload
                        }
                    )
                }
            }, {
                Timber.d("error=$it")
            })
    }
}