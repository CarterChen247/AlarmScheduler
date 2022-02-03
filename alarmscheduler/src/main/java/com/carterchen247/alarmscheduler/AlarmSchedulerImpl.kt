package com.carterchen247.alarmscheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import com.carterchen247.alarmscheduler.constant.Constant
import com.carterchen247.alarmscheduler.error.AlarmSchedulerErrorHandler
import com.carterchen247.alarmscheduler.error.ErrorHandler
import com.carterchen247.alarmscheduler.logger.AlarmSchedulerLogger
import com.carterchen247.alarmscheduler.logger.Logger
import com.carterchen247.alarmscheduler.model.AlarmInfo
import com.carterchen247.alarmscheduler.model.ScheduledAlarmsCallback
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
        Logger.d("Function cancelAlarmTask() invoked")
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
        Logger.d("Function cancelAllAlarmTasks() invoked")
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
        Logger.d("Function rescheduleAlarms() invoked")
        coroutineScope.launch(CoroutineExceptionHandler { _, throwable ->
            ErrorHandler.onError(IllegalStateException("rescheduleAlarms failed", throwable))
        }) {
            alarmStateRepository.getAll()
                .also {
                    Logger.d("Total count of rescheduled alarms=${it.size}")
                }
                .forEach {
                    schedule(it)
                }
        }
    }

    fun schedule(alarmInfo: AlarmInfo): Int {
        val id = idProvider.generateId(alarmInfo.alarmId)
        val calibratedAlarmInfo = alarmInfo.copy(alarmId = id)
        Logger.d("Received alarm scheduling command. AlarmInfo=$calibratedAlarmInfo")

        coroutineScope.launch(CoroutineExceptionHandler { _, throwable ->
            ErrorHandler.onError(IllegalStateException("failed schedule an alarm", throwable))
        }) {
            alarmStateRepository.add(calibratedAlarmInfo)
            scheduleAlarm(calibratedAlarmInfo)
        }
        return id
    }

    private fun scheduleAlarm(alarmInfo: AlarmInfo) {
        Logger.d("Start to schedule alarm. AlarmInfo=$alarmInfo")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) ?: return
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmInfo.alarmId,
            buildIntent(alarmInfo),
            PendingIntent.FLAG_UPDATE_CURRENT
        ) ?: return

        AlarmManagerCompat.setAlarmClock(
            alarmManager as AlarmManager,
            alarmInfo.triggerTime,
            pendingIntent,
            pendingIntent
        )
        Logger.d("The alarm has been scheduled via AlarmManagerCompat")
    }

    private fun buildIntent(alarmInfo: AlarmInfo): Intent {
        return Intent(context, AlarmTriggerReceiver::class.java).apply {
            putExtra(Constant.ALARM_TYPE, alarmInfo.alarmType)
            putExtra(Constant.ALARM_ID, alarmInfo.alarmId)
            putExtra(Constant.ALARM_CUSTOM_DATA, alarmInfo.dataPayload?.getBundle())
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