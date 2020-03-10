package com.carterchen247.alarmscheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmTriggerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmType = intent.getIntExtra(Constant.ALARM_TYPE, Constant.VALUE_NOT_ASSIGN)
        val alarmId = intent.getIntExtra(Constant.ALARM_ID, Constant.VALUE_NOT_ASSIGN)
        val bundle = intent.getBundleExtra(Constant.ALARM_CUSTOM_DATA)
        if (alarmType == Constant.VALUE_NOT_ASSIGN || alarmId == Constant.VALUE_NOT_ASSIGN) {
            error("something wrong")
        }
        val alarmTask = AlarmScheduler.getFactory().createAlarmTask(alarmType)
        alarmTask.onAlarmFires(alarmId, DataPayload.create(bundle))
        AlarmTaskDatabase.getInstance(context).getAlarmTaskDao().removeEntity(AlarmTaskEntity(alarmId))
    }
}