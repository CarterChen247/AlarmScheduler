package com.carterchen247.alarmscheduler


import android.content.Context

internal class AlarmIdProvider(context: Context) {

    private val impl: AlarmIdProviderImpl by lazy { AlarmIdProviderImpl(context) }

    fun generateId(alarmId: Int): Int {
        return impl.generateId(alarmId)
    }

    companion object {
        @Volatile
        private var instance: AlarmIdProvider? = null

        internal fun getInstance(context: Context): AlarmIdProvider {
            return instance ?: synchronized(AlarmIdProvider::class.java) {
                instance ?: AlarmIdProvider(context).also { instance = it }
            }
        }
        const val AUTO_GENERATE = 0
    }
}
