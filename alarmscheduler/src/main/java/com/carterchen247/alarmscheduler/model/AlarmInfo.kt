package com.carterchen247.alarmscheduler.model

import com.carterchen247.alarmscheduler.AlarmIdProvider

data class AlarmInfo(
    val alarmType: Int,
    val triggerTime: Long,
    var alarmId: Int = AlarmIdProvider.AUTO_GENERATE,
    var dataPayload: DataPayload? = null
) {
    fun autoGenerateId(): Boolean {
        return alarmId == AlarmIdProvider.AUTO_GENERATE
    }
}