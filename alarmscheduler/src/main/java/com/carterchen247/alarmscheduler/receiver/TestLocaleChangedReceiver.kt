package com.carterchen247.alarmscheduler.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.carterchen247.alarmscheduler.Constant

class TestLocaleChangedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        queryReceiveApps(context)
    }

    private fun queryReceiveApps(context: Context) {
        val intent = Intent(Constant.ADD_ALARM_TASK_FACTORY)
        intent.setPackage(context.packageName)

        val resolveInfos = context.packageManager.queryBroadcastReceivers(intent, 0)
        resolveInfos.forEach { info ->
            val name = info?.activityInfo?.name ?: return
            val receiver = Class.forName(name).newInstance() as RestartSchedulerReceiver
            receiver.restartScheduler()
        }
    }
}