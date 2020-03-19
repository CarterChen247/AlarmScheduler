package com.carterchen247.alarmscheduler.model

import com.carterchen247.alarmscheduler.AlarmIdCounter

data class AlarmInfo(
    val alarmType: Int,
    val triggerTime: Long,
    var alarmId: Int = AlarmIdCounter.AUTO_GENERATE,
    var dataPayload: DataPayload? = null
) {
    fun autoGenerateId(): Boolean {
        return alarmId == AlarmIdCounter.AUTO_GENERATE
    }
}