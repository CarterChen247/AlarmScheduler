package com.carterchen247.alarmscheduler

import org.junit.Assert.assertEquals
import org.junit.Test

class AlarmConfigTest {

    @Test
    fun testEquality() {
        val dataPayload = DataPayload().apply {
            putString("key", "value")
        }
        val alarmConfig = AlarmConfig(
            alarmType = 1,
            triggerTime = -1,
            dataPayload = dataPayload
        )
        val newConfig = alarmConfig.copy(alarmId = alarmConfig.alarmId)
        assertEquals(alarmConfig, newConfig)
    }
}