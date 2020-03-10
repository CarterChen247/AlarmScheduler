package com.carterchen247.alarmscheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

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
        Timber.d("schedule")
        val d = getAlarmId(config)
            .subscribeOn(Schedulers.io())
            .map { it.toInt() }
            .subscribe({ alarmId ->
                val alarmInfo = config.getAlarmInfo().copy(alarmId = alarmId)
                scheduleInternal(alarmInfo, config.dataPayload)
            }, {
                Timber.e("something wrong")
            })
    }

    private fun getAlarmId(alarmConfig: AlarmConfig): Single<Long> {
        return if (alarmConfig.hasUserAssignedId()) {
            alarmTaskDao.insertEntity(
                AlarmTaskEntity(
                    alarmConfig.alarmId,
                    alarmConfig.dataPayload
                )
            )
        } else {
            alarmTaskDao.insertEntity(
                AlarmTaskEntity(dataPayload = alarmConfig.dataPayload)
            )
        }
    }

    private fun scheduleInternal(alarmInfo: AlarmInfo, dataPayload: DataPayload?) {
        Timber.d("scheduleInternal alarmInfo=$alarmInfo")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) ?: return
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmInfo.alarmId,
            buildIntent(alarmInfo, dataPayload),
            PendingIntent.FLAG_UPDATE_CURRENT
        ) ?: return

        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager as AlarmManager,
            AlarmManager.RTC_WAKEUP,
            alarmInfo.triggerAtMillis,
            pendingIntent
        )
    }

    private fun buildIntent(alarmInfo: AlarmInfo, dataPayload: DataPayload?): Intent {
        return Intent(context, AlarmTriggerReceiver::class.java).apply {
            putExtra(Constant.ALARM_TYPE, alarmInfo.alarmType)
            putExtra(Constant.ALARM_ID, alarmInfo.alarmId)
            putExtra(Constant.ALARM_CUSTOM_DATA, dataPayload?.getBundle())
        }
    }

    fun cancelAlarmTask(alarmId: Int) {
        val pendingIntent = getPendingIntentById(alarmId)
        Timber.d("alarm of id $alarmId exist=${pendingIntent != null}")
        pendingIntent?.let {
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

