package com.carterchen247.alarmscheduler.logger

import android.os.Bundle
import com.carterchen247.alarmscheduler.extension.toMap
import com.carterchen247.alarmscheduler.model.AlarmInfo

object LogMessage {
    fun onCancelAlarmTask() = "Function cancelAlarmTask() invoked"
    fun onCancelAllAlarmTasks() = "Function cancelAllAlarmTasks() invoked"
    fun onRescheduleAlarms() = "Function rescheduleAlarms() invoked"
    fun onCalculateRescheduleAlarmsTotalCount(count: Int) = "Total count of rescheduled alarms=${count}"
    fun onSchedule(alarmInfo: AlarmInfo) = "Received alarm scheduling command. AlarmInfo=$alarmInfo"
    fun onScheduleAlarm(alarmInfo: AlarmInfo) = "Start to schedule alarm. AlarmInfo=$alarmInfo"
    fun onScheduleAlarmSuccessfully() = "The alarm has been scheduled via AlarmManagerCompat"
    fun onAlarmTriggerReceiverOnReceive(alarmType: Int, alarmId: Int, bundle: Bundle?) = "The scheduled alarm goes off. Data payloads of the scheduled alarm: alarmType=$alarmType alarmId=$alarmId bundle data=${bundle.toMap()}"
    fun onCreateAlarmTask(alarmType: Int, alarmId: Int) = "Creating AlarmTask triggering callback. alarmType=$alarmType alarmId=$alarmId"
    fun onRebootCompleteReceiverOnReceive() = "RebootCompleteReceiver.onReceive() invoked"
    fun onRescheduleAlarmServiceOnHandleWork() = "RescheduleAlarmService.onHandleWork() invoked"
    fun onBroadcastReceiverOnReceiveInvoked(receiver: Any) = "onReceive() of ${receiver::class.java.simpleName} invoked"
}