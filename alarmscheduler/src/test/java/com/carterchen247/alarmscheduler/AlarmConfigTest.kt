package com.carterchen247.alarmscheduler

import com.carterchen247.alarmscheduler.model.AlarmConfig
import org.junit.Assert.assertEquals
import org.junit.Test

class AlarmConfigTest {

    @Test
    fun `test getInfo()`() {
        val config = AlarmConfig(1, 2) {
            id(3)
        }
        val alarmInfo = config.getInfo()
        assertEquals(1, alarmInfo.alarmType)
        assertEquals(2, alarmInfo.triggerTime)
        assertEquals(3, alarmInfo.alarmId)
    }
}