package com.carterchen247.alarmscheduler

data class AlarmInfo(
    val alarmType: Int,
    val triggerTime: Long,
    var alarmId: Int = AlarmID.AUTO_GENERATE,
    var dataPayload: DataPayload? = null
) {
    fun hasUserAssignedId(): Boolean {
        return alarmId != AlarmID.AUTO_GENERATE
    }
}