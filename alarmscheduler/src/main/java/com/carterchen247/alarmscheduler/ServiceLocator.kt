package com.carterchen247.alarmscheduler

import com.carterchen247.alarmscheduler.storage.AlarmSchedulerDatabase
import com.carterchen247.alarmscheduler.storage.AlarmStateRepository

object ServiceLocator {
    private val applicationContext by lazy { ApplicationContextHolder.getInstance() }
    private val alarmSchedulerImpl by lazy { AlarmSchedulerImpl(applicationContext) }
    private val database by lazy { AlarmSchedulerDatabase.buildDatabase(applicationContext) }
    private val alarmStateRepository by lazy { AlarmStateRepository(database.getAlarmStateDao()) }

    internal fun provideAlarmSchedulerImpl() = alarmSchedulerImpl
    internal fun provideAlarmStateRepository() = alarmStateRepository
}