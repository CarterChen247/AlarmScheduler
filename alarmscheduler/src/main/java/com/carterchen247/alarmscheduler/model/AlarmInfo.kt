package com.carterchen247.alarmscheduler.model

import com.carterchen247.alarmscheduler.constant.AlarmID

data class AlarmInfo(
    val alarmType: Int,
    val triggerTime: Long,
    var alarmId: Int = AlarmID.AUTO_GENERATE,
    var dataPayload: DataPayload? = null
) {
    fun autoGenerateId(): Boolean {
        return alarmId == AlarmID.AUTO_GENERATE
    }
}