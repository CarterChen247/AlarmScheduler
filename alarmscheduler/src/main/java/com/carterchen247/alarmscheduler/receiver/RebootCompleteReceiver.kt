package com.carterchen247.alarmscheduler.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.carterchen247.alarmscheduler.AlarmInfo
import com.carterchen247.alarmscheduler.AlarmScheduler
import com.carterchen247.alarmscheduler.AlarmTaskDatabase
import com.carterchen247.alarmscheduler.logger.Logger

class RebootCompleteReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        restartScheduler(context)
        rescheduleAlarms(context)
    }

    private fun restartScheduler(context: Context) {
        Logger.d("restartScheduler")
        val intent = Intent(RestartSchedulerReceiver.ACTION)
        intent.setPackage(context.packageName)

        val resolveInfos = context.packageManager.queryBroadcastReceivers(intent, 0)
        resolveInfos.forEach { info ->
            val name = info?.activityInfo?.name ?: return
            val receiver = Class.forName(name).newInstance() as RestartSchedulerReceiver
            receiver.restartScheduler()
        }
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