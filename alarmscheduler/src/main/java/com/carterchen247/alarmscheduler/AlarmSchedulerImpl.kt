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

internal class AlarmSchedulerImpl private constructor(private val context: Context) : AlarmSchedulerContract {

    private var alarmTaskFactory: AlarmTaskFactory? = null
    private var logger = Logger
    private val alarmStateRepository = AlarmStateRepository.getInstance(context)
    private val idProvider by lazy { AlarmIdProvider(context) }

    override fun setAlarmTaskFactory(alarmTaskFactory: AlarmTaskFactory) {
        this.alarmTaskFactory = alarmTaskFactory
    }

    override fun setLogger(logger: AlarmSchedulerLogger?) {
        this.logger.logger = logger
    }

    override fun cancelAlarmTask(alarmId: Int) {
        Logger.d("cancelAlarmTask()")
        getPendingIntentById(alarmId)?.let {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(it)
            it.cancel()
            alarmStateRepository.removeImmediately(alarmId)
        }
    }

    override fun isAlarmTaskRunning(alarmId: Int): Boolean {
        return getPendingIntentById(alarmId) != null
    }

    override fun cancelAllAlarmTasks() {
        Logger.d("cancelAllAlarmTasks()")
        val d = alarmStateRepository.getAll()
            .subscribe { tasks ->
                tasks.forEach {
                    cancelAlarmTask(it.alarmId)
                }
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
        Logger.d("rescheduleAlarms()")
        val d = alarmStateRepository.getAll()
            .subscribe({ alarmInfos ->
                Logger.d("rescheduleAlarms count=${alarmInfos.size}")
                alarmInfos.forEach { schedule(it) }
            }, {
                Logger.e("rescheduleAlarms failed. error=$it")
            })
    }

    fun schedule(alarmInfo: AlarmInfo): Int {
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
        Logger.d("scheduleAlarm() alarm=$alarmInfo")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) ?: return
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmInfo.alarmId,
            buildIntent(alarmInfo),
            PendingIntent.FLAG_UPDATE_CURRENT
        ) ?: return

        Logger.d("AlarmManagerCompat.setExactAndAllowWhileIdle()")
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