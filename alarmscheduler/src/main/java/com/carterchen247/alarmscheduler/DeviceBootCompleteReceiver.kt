package com.carterchen247.alarmscheduler

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.carterchen247.alarmscheduler.logger.Logger

class DeviceBootCompleteReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        rescheduleAlarms(context)
    }

    private fun rescheduleAlarms(context: Context) {
        Logger.d("rescheduleAlarms")
        val d = AlarmTaskDatabase.getInstance(context)
            .getAlarmTaskDao()
            .selectAll()
            .subscribe({ alarmTasks ->
                alarmTasks.forEach {
                    AlarmScheduler.schedule(
                        AlarmInfo(
                            it.type,
                            it.triggerTime,
                            it.id,
                            it.dataPayload
                        )
                    )
                }
            }, {
                Logger.e("rescheduleAlarms failed. error=$it")
            })
    }
}