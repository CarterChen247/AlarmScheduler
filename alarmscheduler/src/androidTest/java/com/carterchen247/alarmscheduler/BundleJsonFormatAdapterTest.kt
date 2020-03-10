package com.carterchen247.alarmscheduler

import android.os.Bundle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BundleJsonFormatAdapterTest {

    private lateinit var adapter: BundleJsonFormatAdapter
    private val data = listOf(
        "key_putInt" to 1,
        "key_putLong" to 2L,
        "key_putVeryLong" to 12345678901234,
        "key_putString" to "string",
        "key_putBoolean" to true
    )

    @Before
    fun setUp() {
        adapter = BundleJsonFormatAdapter()
    }

    @Test
    fun testLegalFormat() {
        val bundle = Bundle()
        bundle.putInt(data[0].first, data[0].second as Int)
        bundle.putLong(data[1].first, data[1].second as Long)
        bundle.putLong(data[2].first, data[2].second as Long)
        bundle.putString(data[3].first, data[3].second as String)
        bundle.putBoolean(data[4].first, data[4].second as Boolean)
        val json = adapter.toJson(bundle)

        val parsedBundle = adapter.fromJson(json)
        assertEquals(data[0].second, parsedBundle.get(data[0].first))
        // keep in mind that if a number is not greater than Int.MAX_VALUE
        // it will be consider as a Int in JsonObject
        assertEquals(data[1].second, (parsedBundle.get(data[1].first) as Int).toLong())
        assertEquals(data[2].second, parsedBundle.get(data[2].first))
        assertEquals(data[3].second, parsedBundle.get(data[3].first))
        assertEquals(data[4].second, parsedBundle.get(data[4].first))
    }

    @Test
    fun testInvalidJson() {
        val bundle1 = adapter.fromJson("")
        assertTrue(bundle1.size() == 0)

        val bundle2 = adapter.fromJson("{}")
        assertTrue(bundle2.size() == 0)
    }
}