package com.carterchen247.alarmscheduler

data class AlarmConfig(
    val alarmType: Int,
    val triggerTime: Long,
    var alarmId: Int = Constant.AUTO_ASSIGN,
    var dataPayload: DataPayload? = null
) {
    fun hasUserAssignedId(): Boolean {
        return alarmId != Constant.AUTO_ASSIGN
    }
}