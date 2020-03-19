package com.carterchen247.alarmscheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import com.carterchen247.alarmscheduler.constant.Constant
import com.carterchen247.alarmscheduler.logger.AlarmSchedulerLogger
import com.carterchen247.alarmscheduler.logger.Logger
import com.carterchen247.alarmscheduler.model.AlarmInfo
import com.carterchen247.alarmscheduler.receiver.AlarmTriggerReceiver
import com.carterchen247.alarmscheduler.storage.AlarmStateRepository
import com.carterchen247.alarmscheduler.task.AlarmTaskFactory

class AlarmScheduler private constructor(private val context: Context) {

    private var alarmTaskFactory: AlarmTaskFactory? = null
    private var logger = Logger
    private val alarmStateRepository = AlarmStateRepository.getInstance(context)
    private val idProvider = AlarmIdCounter.getInstance(context)

    companion object {
        @Volatile
        private var instance: AlarmScheduler? = null

        internal fun getInstance(context: Context): AlarmScheduler {
            return instance ?: synchronized(AlarmScheduler::class.java) {
                instance ?: AlarmScheduler(context).also { instance = it }
            }
        }

        internal fun getInstance(): AlarmScheduler {
            return instance ?: error("AlarmScheduler instance is null. Please init it first")
        }

        fun init(context: Context) {
            getInstance(context)
        }

        fun setAlarmTaskFactory(alarmTaskFactory: AlarmTaskFactory) {
            getInstance().alarmTaskFactory = alarmTaskFactory
        }

        fun setLogger(logger: AlarmSchedulerLogger?) {
            getInstance().logger.logger = logger
        }
    }

    internal fun schedule(alarmInfo: AlarmInfo): Int {
        val id = idProvider.generateId(alarmInfo.alarmId)
        val calibratedAlarmInfo = alarmInfo.copy(alarmId = id)
        Logger.d("schedule alarm=$calibratedAlarmInfo")
        val d = alarmStateRepository.add(calibratedAlarmInfo)
            .subscribe({
                scheduleAlarm(calibratedAlarmInfo)
            }, {
                Logger.e("failed getting alarm id when scheduling. error=$it")
            })
        return id
    }

    private fun scheduleAlarm(alarmInfo: AlarmInfo) {
        Logger.d("scheduleAlarm alarm=$alarmInfo")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) ?: return
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmInfo.alarmId,
            buildIntent(alarmInfo),
            PendingIntent.FLAG_UPDATE_CURRENT
        ) ?: return

        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager as AlarmManager,
            AlarmManager.RTC_WAKEUP,
            alarmInfo.triggerTime,
            pendingIntent
        )
    }

    private fun buildIntent(alarmInfo: AlarmInfo): Intent {
        return Intent(context, AlarmTriggerReceiver::class.java).apply {
            putExtra(Constant.ALARM_TYPE, alarmInfo.alarmType)
            putExtra(Constant.ALARM_ID, alarmInfo.alarmId)
            putExtra(Constant.ALARM_CUSTOM_DATA, alarmInfo.dataPayload?.getBundle())
        }
    }

    fun cancelAlarmTask(alarmId: Int) {
        getPendingIntentById(alarmId)?.let {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(it)
            it.cancel()
            alarmStateRepository.removeImmediately(alarmId)
        }
    }

    fun cancelAllAlarmTasks() {
        val d = alarmStateRepository.getAll()
            .subscribe { tasks ->
                tasks.forEach {
                    cancelAlarmTask(it.alarmId)
                }
            }
    }

    fun isAlarmTaskRunning(alarmId: Int): Boolean {
        return getPendingIntentById(alarmId) != null
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
        val d = alarmStateRepository.getAll()
            .subscribe({ alarmInfos ->
                Logger.d("rescheduleAlarms count=${alarmInfos.size}")
                alarmInfos.forEach { schedule(it) }
            }, {
                Logger.e("rescheduleAlarms failed. error=$it")
            })
    }

    internal fun getAlarmTaskFactory(): AlarmTaskFactory? {
        return alarmTaskFactory
    }
}

