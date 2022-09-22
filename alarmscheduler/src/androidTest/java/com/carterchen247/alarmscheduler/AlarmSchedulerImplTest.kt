package com.carterchen247.alarmscheduler

import android.app.AlarmManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.carterchen247.alarmscheduler.compat.CompatService
import com.carterchen247.alarmscheduler.model.AlarmConfig
import com.carterchen247.alarmscheduler.model.AlarmInfo
import com.carterchen247.alarmscheduler.model.ScheduleResult
import com.carterchen247.alarmscheduler.model.ScheduleResultCallback
import com.carterchen247.alarmscheduler.storage.AlarmStateDataSource
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
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

    @MockK
    private lateinit var compatService: CompatService

    private val alarmManager by lazy { ApplicationProvider.getApplicationContext<Context>().getSystemService(Context.ALARM_SERVICE) as AlarmManager }
    private lateinit var alarmSchedulerImpl: AlarmSchedulerImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        val dataSource = createAlarmStateDataSource()
        alarmSchedulerImpl = AlarmSchedulerImpl(
            ApplicationProvider.getApplicationContext(),
            alarmManager,
            dataSource,
            compatService
        )
    }

    @Test
    fun scheduleReturnSuccessByDefault() = runBlockingTest {
        val config = AlarmConfig(0, 0)
        val slot = slot<ScheduleResult>()
        val callback = mockk<ScheduleResultCallback>()
        every { callback.onResult(capture(slot)) } just Runs
        every { compatService.canScheduleExactAlarmsCompat() } returns true

        alarmSchedulerImpl.schedule(config, callback)
        val scheduleResult = slot.captured
        assertTrue(scheduleResult is ScheduleResult.Success)
    }

    @Test
    fun scheduleReturnFailureIfExactAlarmPermissionNotGranted() = runBlockingTest {
        val config = AlarmConfig(0, 0)
        val slot = slot<ScheduleResult>()
        val callback = mockk<ScheduleResultCallback>()
        every { callback.onResult(capture(slot)) } just Runs
        every { compatService.canScheduleExactAlarmsCompat() } returns false

        alarmSchedulerImpl.schedule(config, callback)
        val scheduleResult = slot.captured
        assertTrue(scheduleResult is ScheduleResult.Failure)
    }


    private fun createAlarmStateDataSource() = object : AlarmStateDataSource {
        override suspend fun add(alarmInfo: AlarmInfo): Long = 0

        override suspend fun getAll(): List<AlarmInfo> = emptyList()

        override suspend fun removeImmediately(id: Int) = Unit
    }
}