package com.carterchen247.alarmscheduler

import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.carterchen247.alarmscheduler.model.AlarmConfig
import com.carterchen247.alarmscheduler.model.AlarmInfo
import com.carterchen247.alarmscheduler.model.ScheduleResult
import com.carterchen247.alarmscheduler.model.ScheduleResultCallback
import com.carterchen247.alarmscheduler.storage.AlarmStateDataSource
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlarmSchedulerImplTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var alarmSchedulerImpl: AlarmSchedulerImpl

    @Before
    fun setUp() {
        print("xxx setUp")
        val dataSource = createAlarmStateDataSource()
        alarmSchedulerImpl = AlarmSchedulerImpl(
            ApplicationProvider.getApplicationContext(),
            dataSource
        )
    }

    @Test
    fun xxxx() = runBlockingTest {
        println("xxx xxxx")
        Log.d("xxx", "xxxxxx d")
        val config = AlarmConfig(0, 0)
        val slot = slot<ScheduleResult>()
        val callback = mockk<ScheduleResultCallback>()
        every { callback.onResult(capture(slot)) } just Runs

        alarmSchedulerImpl.schedule(config, callback)
        val scheduleResult = slot.captured
        assertTrue(scheduleResult is ScheduleResult.Success)
        assertTrue(true)
    }

    @Test
    fun xxx() {
        val dataSource = object : AlarmStateDataSource {
            override suspend fun add(alarmInfo: AlarmInfo): Long = 0

            override suspend fun getAll(): List<AlarmInfo> = emptyList()

            override suspend fun removeImmediately(id: Int) = Unit
        }
        GlobalScope.launch {
            dataSource.getAll()
        }
    }

    private fun createAlarmStateDataSource() = object : AlarmStateDataSource {
        override suspend fun add(alarmInfo: AlarmInfo): Long = 0

        override suspend fun getAll(): List<AlarmInfo> = emptyList()

        override suspend fun removeImmediately(id: Int) = Unit
    }
}