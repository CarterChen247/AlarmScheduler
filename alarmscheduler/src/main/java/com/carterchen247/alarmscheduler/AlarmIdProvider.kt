package com.carterchen247.alarmscheduler


import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.annotation.VisibleForTesting
import java.util.concurrent.atomic.AtomicInteger

/**
 * responsible for creating unique ids for alarms
 */
internal class AlarmIdProvider(context: Context) {

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

    @VisibleForTesting
    fun clear() {
        sharedPreference.edit().clear().apply()
    }

    companion object {
        private const val PREF_NAME = "PREF_ALARM_ID"
        private const val KEY_ALARM_ID = "KEY_ALARM_ID"
        const val AUTO_GENERATE = 0
    }
}
