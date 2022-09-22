package com.carterchen247.alarmscheduler

import android.app.AlarmManager
import android.content.Context
import com.carterchen247.alarmscheduler.compat.Android12CompatService
import com.carterchen247.alarmscheduler.compat.CompatService
import com.carterchen247.alarmscheduler.storage.AlarmSchedulerDatabase
import com.carterchen247.alarmscheduler.storage.AlarmStateDataSource
import com.carterchen247.alarmscheduler.storage.AlarmStateRepository

@Suppress("RemoveExplicitTypeArguments")
object ServiceLocator {
    private val applicationContext by lazy<Context> { ApplicationContextHolder.getInstance() }
    private val database by lazy<AlarmSchedulerDatabase> { AlarmSchedulerDatabase.buildDatabase(applicationContext) }
    private val alarmStateDataSource by lazy<AlarmStateDataSource> { AlarmStateRepository(database.getAlarmStateDao()) }
    private val alarmManager by lazy<AlarmManager> { applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager }
    private val compatService by lazy<CompatService> { Android12CompatService(alarmManager) }
    private val alarmSchedulerImpl by lazy<AlarmSchedulerImpl> {
        AlarmSchedulerImpl(
            applicationContext,
            alarmManager,
            alarmStateDataSource,
            compatService
        )
    }

    internal fun provideAlarmSchedulerImpl() = alarmSchedulerImpl
    internal fun provideAlarmStateDataSource() = alarmStateDataSource
}