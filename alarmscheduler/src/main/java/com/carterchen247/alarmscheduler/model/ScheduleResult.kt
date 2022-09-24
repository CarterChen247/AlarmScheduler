package com.carterchen247.alarmscheduler.model

import java.io.Serializable

sealed class ScheduleResult : Serializable {
    data class Success(val alarmId: Int) : ScheduleResult()

    sealed class Failure : ScheduleResult() {
        object CannotScheduleExactAlarm : Failure()
        data class Error(val exception: Throwable) : Failure()
    }
}