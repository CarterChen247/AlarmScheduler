package com.carterchen247.alarmscheduler

import androidx.test.core.app.ApplicationProvider
import com.carterchen247.alarmscheduler.model.AlarmConfig
import com.carterchen247.alarmscheduler.model.AlarmInfo
import com.carterchen247.alarmscheduler.model.ScheduleResult
import com.carterchen247.alarmscheduler.model.ScheduleResultCallback
import com.carterchen247.alarmscheduler.storage.AlarmStateDataSource
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AlarmSchedulerImplTest {

    private lateinit var alarmSchedulerImpl: AlarmSchedulerImpl

    @MockK
    private lateinit var callback: ScheduleResultCallback

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        val dataSource = createAlarmStateDataSource()
        alarmSchedulerImpl = AlarmSchedulerImpl(
            ApplicationProvider.getApplicationContext(),
            dataSource
        )
    }

    @Test
    fun name() {
        val config = AlarmConfig(0, 0)
        val slot = slot<ScheduleResult>()
        every { callback.onResult(capture(slot)) } just Runs

        alarmSchedulerImpl.schedule(config, callback)
        val scheduleResult = slot.captured
        assertTrue(scheduleResult is ScheduleResult.Success)
    }

    private fun createAlarmStateDataSource() = object : AlarmStateDataSource {
        override suspend fun add(alarmInfo: AlarmInfo): Long = 0

        override suspend fun getAll(): List<AlarmInfo> = emptyList()

        override suspend fun removeImmediately(id: Int) = Unit
    }
}