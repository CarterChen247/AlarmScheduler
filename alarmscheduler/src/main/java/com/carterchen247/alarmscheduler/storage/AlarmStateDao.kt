package com.carterchen247.alarmscheduler.storage

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
internal interface AlarmStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEntity(entity: AlarmStateEntity): Single<Long>

    @Delete
    fun removeEntity(alarmStateEntity: AlarmStateEntity): Completable

    @Query("SELECT * FROM AlarmStateEntity")
    fun selectAll(): Single<List<AlarmStateEntity>>
}