package com.carterchen247.alarmscheduler

class AlarmConfig(
    val alarmType: Int,
    val triggerAtMillis: Long
) {
    var alarmId: Int = Constant.AUTO_ASSIGN
    var dataPayload: DataPayload? = null

    fun getAlarmInfo() = AlarmInfo(
        alarmType,
        triggerAtMillis,
        alarmId
    )

    fun hasUserAssignedId(): Boolean {
        return alarmId != Constant.AUTO_ASSIGN
    }
}