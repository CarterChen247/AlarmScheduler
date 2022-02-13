package com.carterchen247.alarmscheduler.extension

import com.carterchen247.alarmscheduler.AlarmScheduler
import com.carterchen247.alarmscheduler.model.AlarmConfig

fun scheduleAlarm(
    triggerTime: Long,
    type: Int = 0,
    block: (AlarmConfig.() -> Unit)? = null,
) {
    val config = AlarmConfig(triggerTime, type)
        .apply {
            block?.invoke(this)
        }
    AlarmScheduler.getImpl().schedule(config.getInfo())
}