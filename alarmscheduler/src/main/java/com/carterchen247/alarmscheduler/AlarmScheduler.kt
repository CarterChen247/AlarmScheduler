package com.carterchen247.alarmscheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import io.reactivex.schedulers.Schedulers

object AlarmScheduler {

    private lateinit var context: Context
    private lateinit var alarmTaskFactory: AlarmTaskFactory
    private lateinit var alarmTaskDao: AlarmTaskDao

    fun init(
        context: Context,
        alarmTaskFactory: AlarmTaskFactory
    ) {
        this.context = context
        this.alarmTaskFactory = alarmTaskFactory
        this.alarmTaskDao = AlarmTaskDatabase.getInstance(context).getAlarmTaskDao()
    }

    fun schedule(config: AlarmConfig) {
        schedule(config.getInfo())
    }

    internal fun schedule(alarmInfo: AlarmInfo) {
        Logger.d("schedule alarm=$alarmInfo")
        val d = alarmTaskDao.insertEntity(AlarmTaskEntity.create(alarmInfo))
            .subscribeOn(Schedulers.io())
            .map { it.toInt() }
            .subscribe({ alarmId ->
                val correctIdInfo = alarmInfo.copy(alarmId = alarmId)
                scheduleAlarm(correctIdInfo)
            }, {
                Logger.e("failed getting alarm id when scheduling. error=$it")
            })
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
            alarmTaskDao.removeEntity(AlarmTaskEntity(alarmId))
                .subscribeOn(Schedulers.io())
                .subscribe()
        }
    }

    fun cancelAllAlarmTasks() {
        val d = alarmTaskDao.selectAll()
            .subscribeOn(Schedulers.io())
            .subscribe { tasks ->
                tasks.forEach {
                    cancelAlarmTask(it.id)
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

    internal fun getFactory(): AlarmTaskFactory {
        return alarmTaskFactory
    }
}

