package com.carterchen247.alarmscheduler.receiver

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.carterchen247.alarmscheduler.event.EventDispatcher
import com.carterchen247.alarmscheduler.event.ScheduleExactAlarmPermissionGrantedEvent
import com.carterchen247.alarmscheduler.logger.LogMessage
import com.carterchen247.alarmscheduler.logger.Logger
import com.carterchen247.alarmscheduler.service.RescheduleAlarmService

internal class ScheduleExactAlarmPermissionGrantedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Logger.d(LogMessage.onBroadcastReceiverOnReceiveInvoked(this))
        if (intent.action == AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED) {
            RescheduleAlarmService.startService(context)
            EventDispatcher.dispatchEvent(ScheduleExactAlarmPermissionGrantedEvent)
        }
    }
}