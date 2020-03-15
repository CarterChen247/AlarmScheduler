package com.carterchen247.alarmscheduler.storage

import android.content.Context
import com.carterchen247.alarmscheduler.model.AlarmInfo
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class AlarmStateRepository private constructor(context: Context) {

    private val alarmStateDao = AlarmSchedulerDatabase.getInstance(context).getAlarmStateDao()

    fun add(alarmInfo: AlarmInfo): Single<Long> {
        return alarmStateDao.insertEntity(AlarmStateEntity.create(alarmInfo))
            .subscribeOn(Schedulers.io())
    }

    fun getAll(): Single<List<AlarmInfo>> {
        return alarmStateDao.selectAll()
            .subscribeOn(Schedulers.io())
            .map { entities ->
                entities.map {
                    AlarmInfo(
                        it.type,
                        it.triggerTime,
                        it.id,
                        it.dataPayload
                    )
                }
            }
    }

    fun removeImmediately(id: Int) {
        alarmStateDao.removeEntity(AlarmStateEntity(id = id))
            .subscribeOn(Schedulers.io())
            .subscribe()
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