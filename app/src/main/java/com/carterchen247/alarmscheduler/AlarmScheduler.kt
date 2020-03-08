package com.carterchen247.alarmscheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.AlarmManagerCompat
import androidx.core.os.bundleOf

object AlarmScheduler {

    private lateinit var context: Context
    private lateinit var alarmTaskFactory: AlarmTaskFactory

    fun init(
        context: Context,
        alarmTaskFactory: AlarmTaskFactory
    ) {
        this.context = context
        this.alarmTaskFactory = alarmTaskFactory
    }

    fun schedule(config: AlarmConfig) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) ?: return
        val alarmInfo = config.getAlarmInfo()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmInfo.alarmId,
            buildIntent(alarmInfo, config.getData()),
            PendingIntent.FLAG_UPDATE_CURRENT
        ) ?: return

        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager as AlarmManager,
            AlarmManager.RTC_WAKEUP,
            alarmInfo.triggerAtMillis,
            pendingIntent
        )
    }

    private fun buildIntent(alarmInfo: AlarmInfo, data: Bundle?): Intent {
        return Intent(context, AlarmTriggerReceiver::class.java).apply {
            bundleOf(
                Constant.ALARM_INFO to alarmInfo,
                Constant.DATA to data
            )
        }
    }
}