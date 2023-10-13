package com.carterchen247.alarmscheduler.demo

import com.carterchen247.alarmscheduler.AlarmScheduler
import com.carterchen247.alarmscheduler.demo.log.ListItem
import com.carterchen247.alarmscheduler.event.AlarmSchedulerEventObserver
import com.carterchen247.alarmscheduler.event.ScheduleExactAlarmPermissionGrantedEvent
import com.carterchen247.alarmscheduler.model.AlarmConfig
import com.carterchen247.alarmscheduler.model.ScheduleResult
import java.time.LocalDateTime
import java.util.*

class MainPresenter(
    private val view: MainView
) {
    private val alarmSchedulerEventObserver = createAlarmSchedulerEventObserver()

    fun init() {
        AlarmScheduler.addEventObserver(alarmSchedulerEventObserver)
    }

    fun scheduleAlarm() {
        val config = AlarmConfig(
            Date().time + 10000L,
            DemoAlarmTask.TYPE
        ) {
            dataPayload("reminder" to "have a meeting")
        }
        AlarmScheduler.schedule(config) { result ->
            // result callback is optional
            when (result) {
                is ScheduleResult.Success -> {
                    val now = LocalDateTime.now()
                    view.addListItem(ListItem("The scheduled alarm id = ${result.alarmId}", now.toString()))
                }
                is ScheduleResult.Failure -> {
                    when (result) {
                        ScheduleResult.Failure.CannotScheduleExactAlarm -> {
                            view.showExactAlarmPermissionSetupDialog()
                        }
                        is ScheduleResult.Failure.Error -> {
                            val now = LocalDateTime.now()
                            view.addListItem(ListItem("Alarm scheduling has failed, exception = ${result.exception}", now.toString()))
                        }
                    }
                }
            }
        }
    }

    fun requestScheduledAlarmsInfo() {
        AlarmScheduler.getScheduledAlarmsAsync { scheduledAlarms ->
            val msg = "Scheduled alarms = $scheduledAlarms"
            val now = LocalDateTime.now()
            view.addListItem(ListItem(msg, now.toString()))
        }
    }

    private fun createAlarmSchedulerEventObserver() = AlarmSchedulerEventObserver { event ->
        if (event is ScheduleExactAlarmPermissionGrantedEvent) {
            val now = LocalDateTime.now()
            view.addListItem(ListItem("Permission has been granted for scheduling exact alarms.", now.toString()))
        }
    }

    fun destroy() {
        AlarmScheduler.removeEventObserver(alarmSchedulerEventObserver)
    }
}