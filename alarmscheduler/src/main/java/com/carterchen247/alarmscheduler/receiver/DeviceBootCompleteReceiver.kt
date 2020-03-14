package com.carterchen247.alarmscheduler.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.carterchen247.alarmscheduler.AlarmInfo
import com.carterchen247.alarmscheduler.AlarmScheduler
import com.carterchen247.alarmscheduler.AlarmTaskDatabase
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