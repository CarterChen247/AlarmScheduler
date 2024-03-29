package com.carterchen247.alarmscheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.AlarmManagerCompat
import com.carterchen247.alarmscheduler.compat.CompatService
import com.carterchen247.alarmscheduler.constant.Constant
import com.carterchen247.alarmscheduler.error.AlarmSchedulerErrorHandler
import com.carterchen247.alarmscheduler.error.ErrorHandler
import com.carterchen247.alarmscheduler.error.ExceptionFactory
import com.carterchen247.alarmscheduler.event.AlarmSchedulerEventObserver
import com.carterchen247.alarmscheduler.event.EventDispatcher
import com.carterchen247.alarmscheduler.extension.toBundle
import com.carterchen247.alarmscheduler.logger.AlarmSchedulerLogger
import com.carterchen247.alarmscheduler.logger.LogMessage
import com.carterchen247.alarmscheduler.logger.Logger
import com.carterchen247.alarmscheduler.model.*
import com.carterchen247.alarmscheduler.receiver.AlarmTriggerReceiver
import com.carterchen247.alarmscheduler.storage.AlarmStateDataSource
import com.carterchen247.alarmscheduler.task.AlarmTaskFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class AlarmSchedulerImpl(
    private val context: Context,
    private val alarmManager: AlarmManager,
    private val alarmStateDataSource: AlarmStateDataSource,
    private val compatService: CompatService,
) : AlarmSchedulerContract {

    private var alarmTaskFactory: AlarmTaskFactory? = null
    private var errorHandler = ErrorHandler
    private val idProvider by lazy { AlarmIdProvider(context) }

    override fun setAlarmTaskFactory(alarmTaskFactory: AlarmTaskFactory) {
        this.alarmTaskFactory = alarmTaskFactory
    }

    override fun setLogger(loggerImpl: AlarmSchedulerLogger) {
        Logger.setDelegate(loggerImpl)
    }

    override fun setErrorHandler(errorHandler: AlarmSchedulerErrorHandler) {
        this.errorHandler.handler = errorHandler
    }

    override fun isAlarmTaskScheduled(alarmId: Int): Boolean {
        return getPendingIntentById(alarmId) != null
    }

    override fun cancelAlarmTask(alarmId: Int) {
        Logger.info(LogMessage.onCancelAlarmTask())
        getPendingIntentById(alarmId)?.let { pendingIntent ->
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            applicationScope.launch {
                try {
                    alarmStateDataSource.removeAlarm(alarmId)
                } catch (exception: Throwable) {
                    ErrorHandler.handleError(ExceptionFactory.failedToCancelAlarmTask(exception))
                }
            }
        }
    }

    override fun cancelAllAlarmTasks() {
        Logger.info(LogMessage.onCancelAllAlarmTasks())
        applicationScope.launch {
            try {
                alarmStateDataSource.getAlarms()
                    .forEach {
                        cancelAlarmTask(it.alarmId)
                    }
            } catch (exception: Throwable) {
                ErrorHandler.handleError(ExceptionFactory.failedToCancelAllAlarmTasks(exception))
            }
        }
    }

    override fun getScheduledAlarmsAsync(callback: ScheduledAlarmsCallback) {
        applicationScope.launch {
            try {
                val scheduledAlarms = alarmStateDataSource.getAlarms().filter {
                    isAlarmTaskScheduled(it.alarmId)
                }
                callback.onResult(scheduledAlarms)
            } catch (exception: Throwable) {
                ErrorHandler.handleError(ExceptionFactory.failedToGetScheduledAlarmTaskCountAsync(exception))
            }
        }
    }

    override fun addEventObserver(observer: AlarmSchedulerEventObserver) {
        EventDispatcher.addEventObserver(observer)
    }

    override fun removeEventObserver(observer: AlarmSchedulerEventObserver) {
        EventDispatcher.removeObserver(observer)
    }

    override fun schedule(config: AlarmConfig, callback: ScheduleResultCallback?) {
        scheduleAlarm(config.getInfo(), callback)
    }

    override fun canScheduleExactAlarms() = compatService.canScheduleExactAlarmsCompat()

    private fun getPendingIntentById(alarmId: Int): PendingIntent? {
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_NO_CREATE
        }
        val intent = Intent(context, AlarmTriggerReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            flags
        )
    }

    fun rescheduleAlarms() {
        Logger.info(LogMessage.onRescheduleAlarms())
        applicationScope.launch {
            try {
                alarmStateDataSource.getAlarms()
                    .also {
                        Logger.info(LogMessage.onCalculateRescheduleAlarmsTotalCount(it.size))
                    }
                    .forEach { alarmInfo ->
                        scheduleAlarm(alarmInfo, null)
                    }

            } catch (exception: Throwable) {
                ErrorHandler.handleError(ExceptionFactory.failedToRescheduleAlarms(exception))
            }
        }
    }

    private fun scheduleAlarm(alarmInfo: AlarmInfo, callback: ScheduleResultCallback?) {
        Logger.info(LogMessage.onScheduleAlarm(alarmInfo))
        if (!compatService.canScheduleExactAlarmsCompat()) {
            callback?.onResult(ScheduleResult.Failure.CannotScheduleExactAlarm)
            return
        }
        val id = idProvider.generateId(alarmInfo.alarmId)
        val calibratedAlarmInfo = alarmInfo.copy(alarmId = id)
        val pendingIntent = createPendingIntent(calibratedAlarmInfo)
        if (pendingIntent == null) {
            callback?.onResult(ScheduleResult.Failure.Error(ExceptionFactory.nullPendingIntent()))
            return
        }

        applicationScope.launch(Dispatchers.Main) {
            try {
                alarmStateDataSource.addAlarm(calibratedAlarmInfo)
                AlarmManagerCompat.setAlarmClock(
                    alarmManager,
                    calibratedAlarmInfo.triggerTime,
                    pendingIntent,
                    pendingIntent
                )
                callback?.onResult(ScheduleResult.Success(id))
                Logger.info(LogMessage.onScheduleAlarmSuccessfully())
            } catch (exception: Throwable) {
                callback?.onResult(ScheduleResult.Failure.Error(exception))
            }
        }
    }

    private fun createPendingIntent(alarmInfo: AlarmInfo): PendingIntent? {
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        return PendingIntent.getBroadcast(
            context,
            alarmInfo.alarmId,
            buildIntent(alarmInfo),
            flags
        )
    }

    private fun buildIntent(alarmInfo: AlarmInfo): Intent {
        return Intent(context, AlarmTriggerReceiver::class.java).apply {
            putExtra(Constant.ALARM_TYPE, alarmInfo.alarmType)
            putExtra(Constant.ALARM_ID, alarmInfo.alarmId)
            putExtra(Constant.ALARM_CUSTOM_DATA, alarmInfo.dataPayload?.dataMap.orEmpty().toBundle())
        }
    }

    fun getAlarmTaskFactory(): AlarmTaskFactory? {
        return alarmTaskFactory
    }
}