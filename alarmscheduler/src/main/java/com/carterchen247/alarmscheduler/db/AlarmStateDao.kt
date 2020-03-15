package com.carterchen247.alarmscheduler.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface AlarmStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEntity(entity: AlarmStateEntity): Single<Long>

    @Delete
    fun removeEntity(alarmStateEntity: AlarmStateEntity): Completable

    @Query("SELECT * FROM AlarmStateEntity")
    fun selectAll(): Single<List<AlarmStateEntity>>
}