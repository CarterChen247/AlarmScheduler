package com.carterchen247.alarmscheduler.model

import com.carterchen247.alarmscheduler.AlarmIdProvider

internal data class AlarmInfo(
    val alarmType: Int,
    val triggerTime: Long,
    var alarmId: Int = AlarmIdProvider.AUTO_GENERATE,
    var dataPayload: DataPayload? = null
)