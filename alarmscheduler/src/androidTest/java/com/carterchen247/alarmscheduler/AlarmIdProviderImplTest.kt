package com.carterchen247.alarmscheduler

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AlarmIdProviderImplTest {

    private lateinit var impl: AlarmIdProviderImpl

    @Before
    fun setUp() {
        val appContext: Context = ApplicationProvider.getApplicationContext()
        impl = AlarmIdProviderImpl(appContext)
    }

    @After
    fun tearDown() {
        impl.clear()
    }

    @Test
    fun testWithAssignedId() {
        val id = impl.generateId(1)
        assertEquals(1, id)
    }

    @Test
    fun testWithoutAssignedId() {
        var id: Int = impl.generateId(AlarmIdProvider.AUTO_GENERATE)
        assertEquals(1, id)

        id = impl.generateId(AlarmIdProvider.AUTO_GENERATE)
        assertEquals(2, id)

        id = impl.generateId(AlarmIdProvider.AUTO_GENERATE)
        assertEquals(3, id)
    }

    @Test
    fun testWithAssignedIdThenWithoutAssignedId() {
        var id: Int = impl.generateId(50)
        assertEquals(50, id)

        id = impl.generateId(AlarmIdProvider.AUTO_GENERATE)
        assertEquals(51, id)

        id = impl.generateId(AlarmIdProvider.AUTO_GENERATE)
        assertEquals(52, id)
    }
}

