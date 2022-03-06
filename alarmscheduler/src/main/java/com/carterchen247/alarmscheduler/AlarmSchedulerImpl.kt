package com.carterchen247.alarmscheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.AlarmManagerCompat
import com.carterchen247.alarmscheduler.constant.Constant
import com.carterchen247.alarmscheduler.error.AlarmSchedulerErrorHandler
import com.carterchen247.alarmscheduler.error.CannotScheduleExactAlarmsException
import com.carterchen247.alarmscheduler.error.ErrorHandler
import com.carterchen247.alarmscheduler.event.AlarmSchedulerEventObserver
import com.carterchen247.alarmscheduler.event.EventDispatcher
import com.carterchen247.alarmscheduler.extension.canScheduleExactAlarmsCompat
import com.carterchen247.alarmscheduler.extension.toBundle
import com.carterchen247.alarmscheduler.logger.AlarmSchedulerLogger
import com.carterchen247.alarmscheduler.logger.LogMessage
import com.carterchen247.alarmscheduler.logger.Logger
import com.carterchen247.alarmscheduler.model.*
import com.carterchen247.alarmscheduler.receiver.AlarmTriggerReceiver
import com.carterchen247.alarmscheduler.storage.AlarmStateRepository
import com.carterchen247.alarmscheduler.task.AlarmTaskFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal class AlarmSchedulerImpl private constructor(
    private val context: Context
) : AlarmSchedulerContract {

    private var alarmTaskFactory: AlarmTaskFactory? = null
    private var logger = Logger
    private var errorHandler = ErrorHandler
    private val alarmStateRepository = AlarmStateRepository.getInstance(context)
    private val idProvider by lazy { AlarmIdProvider(context) }
    val coroutineScope by lazy { CoroutineScope(SupervisorJob()) }

    override fun setAlarmTaskFactory(alarmTaskFactory: AlarmTaskFactory) {
        this.alarmTaskFactory = alarmTaskFactory
    }

    override fun setLogger(logger: AlarmSchedulerLogger?) {
        this.logger.logger = logger
    }

    override fun setErrorHandler(errorHandler: AlarmSchedulerErrorHandler) {
        this.errorHandler.handler = errorHandler
    }

    override fun isAlarmTaskScheduled(alarmId: Int): Boolean {
        return getPendingIntentById(alarmId) != null
    }

    override fun cancelAlarmTask(alarmId: Int) {
        Logger.d(LogMessage.onCancelAlarmTask())
        getPendingIntentById(alarmId)?.let { pendingIntent ->
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            coroutineScope.launch(CoroutineExceptionHandler { _, throwable ->
                ErrorHandler.onError(IllegalStateException("cancelAlarmTask failed", throwable))
            }) {
                alarmStateRepository.removeImmediately(alarmId)
            }
        }
    }

    override fun cancelAllAlarmTasks() {
        Logger.d(LogMessage.onCancelAllAlarmTasks())
        coroutineScope.launch(CoroutineExceptionHandler { _, throwable ->
            ErrorHandler.onError(IllegalStateException("cancelAllAlarmTasks failed", throwable))
        }) {
            alarmStateRepository.getAll()
                .forEach {
                    cancelAlarmTask(it.alarmId)
                }
        }
    }

    override fun getScheduledAlarmsAsync(callback: ScheduledAlarmsCallback) {
        coroutineScope.launch(CoroutineExceptionHandler { _, throwable ->
            ErrorHandler.onError(IllegalStateException("getScheduledAlarmTaskCountAsync failed", throwable))
        }) {
            val scheduledAlarms = alarmStateRepository.getAll().filter {
                isAlarmTaskScheduled(it.alarmId)
            }
            callback.onResult(scheduledAlarms)
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

    private fun getPendingIntentById(alarmId: Int): PendingIntent? {
        val intent = Intent(context, AlarmTriggerReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_NO_CREATE
        )
    }

    fun rescheduleAlarms() {
        Logger.d(LogMessage.onRescheduleAlarms())
        coroutineScope.launch(CoroutineExceptionHandler { _, throwable ->
            ErrorHandler.onError(IllegalStateException("rescheduleAlarms failed", throwable))
        }) {
            alarmStateRepository.getAll()
                .also {
                    Logger.d(LogMessage.onCalculateRescheduleAlarmsTotalCount(it.size))
                }
                .forEach { alarmInfo ->
                    scheduleAlarm(alarmInfo, null)
                }
        }
    }

    private fun scheduleAlarm(alarmInfo: AlarmInfo, callback: ScheduleResultCallback?) {
        Logger.d(LogMessage.onScheduleAlarm(alarmInfo))
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        if (alarmManager == null) {
            callback?.onResult(ScheduleResult.Failure(IllegalStateException("AlarmManager should not be null")))
            return
        }
        if (!alarmManager.canScheduleExactAlarmsCompat()) {
            callback?.onResult(ScheduleResult.Failure(CannotScheduleExactAlarmsException()))
            return
        }
        val id = idProvider.generateId(alarmInfo.alarmId)
        val calibratedAlarmInfo = alarmInfo.copy(alarmId = id)
        val pendingIntent = createPendingIntent(calibratedAlarmInfo)
        if (pendingIntent == null) {
            callback?.onResult(ScheduleResult.Failure(IllegalStateException("PendingIntent should not be null")))
            return
        }
        coroutineScope.launch {
            try {
                alarmStateRepository.add(calibratedAlarmInfo)
                AlarmManagerCompat.setAlarmClock(
                    alarmManager,
                    calibratedAlarmInfo.triggerTime,
                    pendingIntent,
                    pendingIntent
                )
                callback?.onResult(ScheduleResult.Success(id))
                Logger.d(LogMessage.onScheduleAlarmSuccessfully())
            } catch (exception: Throwable) {
                callback?.onResult(ScheduleResult.Failure(Exception("failed scheduling the alarm", exception)))
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

    companion object {
        @Volatile
        private var instance: AlarmSchedulerImpl? = null

        fun getInstance(context: Context): AlarmSchedulerImpl {
            return instance ?: synchronized(AlarmSchedulerImpl::class.java) {
                instance ?: AlarmSchedulerImpl(context).also { instance = it }
            }
        }
    }
}