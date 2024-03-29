package com.carterchen247.alarmscheduler

import com.carterchen247.alarmscheduler.model.AlarmInfo
import com.carterchen247.alarmscheduler.model.DataPayload
import org.junit.Assert.assertEquals
import org.junit.Test

class AlarmInfoTest {

    @Test
    fun testEquality() {
        val dataPayload = DataPayload.of("key" to "value")
        val alarmConfig = AlarmInfo(
            alarmType = 1,
            triggerTime = -1,
            dataPayload = dataPayload
        )
        val newConfig = alarmConfig.copy(alarmId = alarmConfig.alarmId)
        assertEquals(alarmConfig, newConfig)
    }
}