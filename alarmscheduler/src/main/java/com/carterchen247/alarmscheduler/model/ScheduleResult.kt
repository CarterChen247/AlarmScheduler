package com.carterchen247.alarmscheduler.model

import java.io.Serializable

sealed class ScheduleResult : Serializable {
    data class Success(val alarmId: Int) : ScheduleResult()
    data class Failure(val exception: Throwable) : ScheduleResult()
}