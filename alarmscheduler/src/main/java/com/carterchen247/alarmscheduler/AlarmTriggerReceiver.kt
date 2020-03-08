package com.carterchen247.alarmscheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmTriggerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmInfo = intent.getParcelableExtra<AlarmInfo>(Constant.ALARM_INFO)
        val customData = intent.getBundleExtra(Constant.CUSTOM_DATA)
        if (alarmInfo == null) {
            error("alarmInfo should not be null here")
        }
        val alarmTask = AlarmScheduler.alarmTaskFactory.createAlarmTask(alarmInfo.alarmType)
        alarmTask.onAlarmFires(alarmInfo, customData)
    }
}