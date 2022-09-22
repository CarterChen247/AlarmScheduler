package com.carterchen247.alarmscheduler.storage

import com.carterchen247.alarmscheduler.model.AlarmInfo

internal class AlarmStateRepository(
    private val alarmStateDao: AlarmStateDao
) : AlarmStateDataSource {

    override suspend fun addAlarm(alarmInfo: AlarmInfo): Long {
        return alarmStateDao.insertEntity(AlarmStateEntity.create(alarmInfo))
    }

    override suspend fun getAlarms(): List<AlarmInfo> {
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

    override suspend fun removeAlarm(alarmId: Int) {
        alarmStateDao.removeEntity(AlarmStateEntity(id = alarmId))
    }
}