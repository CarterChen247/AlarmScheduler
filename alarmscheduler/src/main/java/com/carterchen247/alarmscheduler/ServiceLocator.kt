package com.carterchen247.alarmscheduler

object ServiceLocator {
    private val applicationContext by lazy { ApplicationContextHolder.getInstance() }
    private val alarmSchedulerImpl by lazy { AlarmSchedulerImpl(applicationContext) }

    internal fun provideAlarmSchedulerImpl() = alarmSchedulerImpl
}