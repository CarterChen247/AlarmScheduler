package com.carterchen247.alarmscheduler


import android.content.Context
import android.content.Context.MODE_PRIVATE
import java.util.concurrent.atomic.AtomicInteger

class AlarmIdCounter(context: Context) {

    private val sharedPreference = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
    private val id by lazy { AtomicInteger(sharedPreference.getInt(KEY_ALARM_ID, 0)) }

    fun generateId(alarmId: Int): Int {
        if (alarmId != AUTO_GENERATE) {
            sharedPreference.edit().putInt(KEY_ALARM_ID, alarmId).apply()
            return alarmId
        }
        val generatedId = id.incrementAndGet()
        sharedPreference.edit().putInt(KEY_ALARM_ID, generatedId).apply()
        return generatedId
    }

    companion object {
        @Volatile
        private var instance: AlarmIdCounter? = null

        internal fun getInstance(context: Context): AlarmIdCounter {
            return instance ?: synchronized(AlarmIdCounter::class.java) {
                instance ?: AlarmIdCounter(context).also { instance = it }
            }
        }

        private const val PREF_NAME = "pref_alarm_id"
        private const val KEY_ALARM_ID = "KEY_ALARM_ID"
        const val AUTO_GENERATE = 0
    }
}
