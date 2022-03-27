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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlarmSchedulerImplTest {

    private lateinit var alarmSchedulerImpl: AlarmSchedulerImpl
    val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        print("xxx setUp")
        val dataSource = createAlarmStateDataSource()
        alarmSchedulerImpl = AlarmSchedulerImpl(
            ApplicationProvider.getApplicationContext(),
            dataSource
        )
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun xxxx() = testDispatcher.runBlockingTest {
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

    private fun createAlarmStateDataSource() = object : AlarmStateDataSource {
        override suspend fun add(alarmInfo: AlarmInfo): Long = 0

        override suspend fun getAll(): List<AlarmInfo> = emptyList()

        override suspend fun removeImmediately(id: Int) = Unit
    }
}