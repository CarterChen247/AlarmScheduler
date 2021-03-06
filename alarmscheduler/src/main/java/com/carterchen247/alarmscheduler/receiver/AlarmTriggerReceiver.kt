package com.carterchen247.alarmscheduler.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.carterchen247.alarmscheduler.AlarmScheduler
import com.carterchen247.alarmscheduler.constant.Constant
import com.carterchen247.alarmscheduler.error.ErrorHandler
import com.carterchen247.alarmscheduler.logger.Logger
import com.carterchen247.alarmscheduler.model.DataPayload
import com.carterchen247.alarmscheduler.storage.AlarmStateRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

internal class AlarmTriggerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Logger.d("AlarmTriggerReceiver.onReceive")
        val alarmType = intent.getIntExtra(Constant.ALARM_TYPE, Constant.VALUE_NOT_ASSIGN)
        val alarmId = intent.getIntExtra(Constant.ALARM_ID, Constant.VALUE_NOT_ASSIGN)
        val bundle = intent.getBundleExtra(Constant.ALARM_CUSTOM_DATA)
        Logger.d("AlarmTriggerReceiver.onReceive alarmType=$alarmType alarmId=$alarmId bundle=$bundle")
        if (alarmType == Constant.VALUE_NOT_ASSIGN || alarmId == Constant.VALUE_NOT_ASSIGN) {
            return
        }
        val alarmTaskFactory = AlarmScheduler.getImpl().getAlarmTaskFactory()
        if (alarmTaskFactory == null) {
            ErrorHandler.onError(IllegalStateException("Failed creating AlarmTask, alarmTaskFactory is null"))
            return
        }
        Logger.d("Creating AlarmTask. alarmType=$alarmType alarmId=$alarmId")
        try {
            val alarmTask = alarmTaskFactory.createAlarmTask(alarmType)
            alarmTask.onAlarmFires(alarmId, DataPayload.create(bundle))
        } catch (throwable: Throwable) {
            ErrorHandler.onError(IllegalStateException("Failed creating AlarmTask", throwable))
        }
        AlarmScheduler.getImpl().coroutineScope.launch(CoroutineExceptionHandler { _, throwable ->
            ErrorHandler.onError(IllegalStateException("Failed removing fired alarmId", throwable))
        }) {
            AlarmStateRepository.getInstance(context).removeImmediately(alarmId)
        }
    }
}