package com.carterchen247.alarmscheduler.storage

import com.carterchen247.alarmscheduler.model.AlarmInfo

internal class AlarmStateRepository(
    private val alarmStateDao: AlarmStateDao
) {

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
}