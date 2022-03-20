package com.carterchen247.alarmscheduler.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.carterchen247.alarmscheduler.AlarmSchedulerImpl
import com.carterchen247.alarmscheduler.applicationScope
import com.carterchen247.alarmscheduler.constant.Constant
import com.carterchen247.alarmscheduler.error.ErrorHandler
import com.carterchen247.alarmscheduler.error.ExceptionFactory
import com.carterchen247.alarmscheduler.extension.toMap
import com.carterchen247.alarmscheduler.logger.LogMessage
import com.carterchen247.alarmscheduler.logger.Logger
import com.carterchen247.alarmscheduler.model.DataPayload
import com.carterchen247.alarmscheduler.storage.AlarmStateRepository
import kotlinx.coroutines.launch

internal class AlarmTriggerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Logger.info(LogMessage.onBroadcastReceiverOnReceiveInvoked(this))

        val alarmType = intent.getIntExtra(Constant.ALARM_TYPE, Constant.VALUE_NOT_ASSIGN)
        val alarmId = intent.getIntExtra(Constant.ALARM_ID, Constant.VALUE_NOT_ASSIGN)
        val bundle = intent.getBundleExtra(Constant.ALARM_CUSTOM_DATA)
        Logger.info(LogMessage.onAlarmTriggerReceiverOnReceive(alarmType, alarmId, bundle))

        if (alarmType == Constant.VALUE_NOT_ASSIGN || alarmId == Constant.VALUE_NOT_ASSIGN) {
            return
        }
        val alarmTaskFactory = AlarmSchedulerImpl.getInstance().getAlarmTaskFactory()
        if (alarmTaskFactory == null) {
            ErrorHandler.onError(ExceptionFactory.nullAlarmTaskFactory())
            return
        }

        Logger.info(LogMessage.onCreateAlarmTask(alarmType, alarmId))
        try {
            val alarmTask = alarmTaskFactory.createAlarmTask(alarmType)
            alarmTask.onAlarmFires(alarmId, DataPayload(bundle.toMap()))
        } catch (throwable: Throwable) {
            ErrorHandler.onError(ExceptionFactory.failedToCreateAlarmTask(throwable))
        }
        applicationScope.launch {
            try {
                AlarmStateRepository.getInstance(context).removeImmediately(alarmId)
            } catch (exception: Throwable) {
                ErrorHandler.onError(ExceptionFactory.failedToRemoveAlarmState(exception))
            }
        }
    }
}