package com.carterchen247.alarmscheduler.storage

import android.content.Context
import com.carterchen247.alarmscheduler.model.AlarmInfo

internal class AlarmStateRepository private constructor(context: Context) {

    private val alarmStateDao = AlarmSchedulerDatabase.getInstance(context).getAlarmStateDao()

    suspend fun add(alarmInfo: AlarmInfo): Long {
        return alarmStateDao.insertEntity(AlarmStateEntity.create(alarmInfo))
    }

    suspend fun getAll(): List<AlarmInfo> {
        return alarmStateDao.selectAll()
            .map {
                AlarmInfo(
                    it.type,
                    it.triggerTime,
                    it.id,
                    it.dataPayload
                )
            }
    }

    suspend fun removeImmediately(id: Int) {
        alarmStateDao.removeEntity(AlarmStateEntity(id = id))
    }

    companion object {
        @Volatile
        private var instance: AlarmStateRepository? = null

        internal fun getInstance(context: Context): AlarmStateRepository {
            return instance ?: synchronized(AlarmStateRepository::class.java) {
                instance ?: AlarmStateRepository(context).also { instance = it }
            }
        }
    }
}