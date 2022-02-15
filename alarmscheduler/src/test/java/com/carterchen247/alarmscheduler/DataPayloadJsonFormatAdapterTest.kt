package com.carterchen247.alarmscheduler

import com.carterchen247.alarmscheduler.model.DataPayload
import com.carterchen247.alarmscheduler.storage.converter.DataPayloadJsonFormatAdapter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DataPayloadJsonFormatAdapterTest {

    companion object {
        private const val KEY_PUT_INT = "key_putInt"
        private const val KEY_PUT_LONG = "key_putLong"
        private const val KEY_PUT_VERY_LONG = "key_putVeryLong"
        private const val KEY_PUT_STRING = "key_putString"
        private const val KEY_PUT_BOOLEAN = "key_putBoolean"
    }

    private lateinit var adapter: DataPayloadJsonFormatAdapter
    private val pairs = listOf(
        KEY_PUT_INT to 1,
        KEY_PUT_LONG to 2L,
        KEY_PUT_VERY_LONG to 12345678901234,
        KEY_PUT_STRING to "string",
        KEY_PUT_BOOLEAN to true
    )

    @Before
    fun setUp() {
        adapter = DataPayloadJsonFormatAdapter()
    }

    @Test
    fun testLegalFormat() {
        val pairArray = pairs.toTypedArray()
        val dataPayload = DataPayload.of(*pairArray)
        val json = adapter.toJson(dataPayload)

        val parsedDataPayload = adapter.fromJson(json)
        val parsedDataMap = parsedDataPayload.dataMap
        assertEquals(1, parsedDataMap[KEY_PUT_INT])
        // keep in mind that if a number is not greater than Int.MAX_VALUE
        // it will be consider as a Int in JsonObject
        assertEquals(2L, (parsedDataMap[KEY_PUT_LONG] as Int).toLong())
        assertEquals(12345678901234, parsedDataMap[KEY_PUT_VERY_LONG])
        assertEquals("string", parsedDataMap[KEY_PUT_STRING])
        assertEquals(true, parsedDataMap[KEY_PUT_BOOLEAN])
    }

    @Test
    fun testInvalidJson() {
        val dataPayload1 = adapter.fromJson("")
        assertTrue(dataPayload1.dataMap.isEmpty())

        val dataPayload2 = adapter.fromJson("{}")
        assertTrue(dataPayload2.dataMap.isEmpty())
    }
}