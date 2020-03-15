package com.carterchen247.alarmscheduler.task

import com.carterchen247.alarmscheduler.model.DataPayload

interface AlarmTask {
    fun onAlarmFires(alarmId: Int, dataPayload: DataPayload)
}