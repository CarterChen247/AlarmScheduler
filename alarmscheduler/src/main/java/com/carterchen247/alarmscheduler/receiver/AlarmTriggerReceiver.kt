package com.carterchen247.alarmscheduler.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.carterchen247.alarmscheduler.*
import com.carterchen247.alarmscheduler.logger.Logger
import io.reactivex.schedulers.Schedulers

class AlarmTriggerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmType = intent.getIntExtra(Constant.ALARM_TYPE, Constant.VALUE_NOT_ASSIGN)
        val alarmId = intent.getIntExtra(Constant.ALARM_ID, Constant.VALUE_NOT_ASSIGN)
        val bundle = intent.getBundleExtra(Constant.ALARM_CUSTOM_DATA)
        if (alarmType == Constant.VALUE_NOT_ASSIGN || alarmId == Constant.VALUE_NOT_ASSIGN) {
            return
        }
        val alarmTaskFactory = AlarmScheduler.getInstance(context).getAlarmTaskFactory()
        if (alarmTaskFactory == null) {
            Logger.d("Failed creating AlarmTask, alarmTaskFactory is null")
            return
        }
        Logger.d("Creating AlarmTask. alarmType=$alarmType alarmId=$alarmId")
        val alarmTask = alarmTaskFactory.createAlarmTask(alarmType)
        alarmTask.onAlarmFires(alarmId, DataPayload.create(bundle))
        AlarmTaskDatabase.getInstance(context).getAlarmTaskDao().removeEntity(AlarmTaskEntity(alarmId))
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}