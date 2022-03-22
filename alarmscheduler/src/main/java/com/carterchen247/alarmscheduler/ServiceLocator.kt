package com.carterchen247.alarmscheduler

import android.content.Context
import com.carterchen247.alarmscheduler.storage.AlarmSchedulerDatabase
import com.carterchen247.alarmscheduler.storage.AlarmStateDataSource
import com.carterchen247.alarmscheduler.storage.AlarmStateRepository

@Suppress("RemoveExplicitTypeArguments")
object ServiceLocator {
    private val applicationContext by lazy<Context> { ApplicationContextHolder.getInstance() }
    private val database by lazy<AlarmSchedulerDatabase> { AlarmSchedulerDatabase.buildDatabase(applicationContext) }
    private val alarmStateDataSource by lazy<AlarmStateDataSource> { AlarmStateRepository(database.getAlarmStateDao()) }
    private val alarmSchedulerImpl by lazy<AlarmSchedulerImpl> { AlarmSchedulerImpl(applicationContext, alarmStateDataSource) }

    internal fun provideAlarmSchedulerImpl() = alarmSchedulerImpl
    internal fun provideAlarmStateDataSource() = alarmStateDataSource
}