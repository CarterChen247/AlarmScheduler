package com.carterchen247.alarmscheduler.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface AlarmTaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEntity(entity: AlarmTaskEntity): Single<Long>

    @Delete
    fun removeEntity(alarmTaskEntity: AlarmTaskEntity): Completable

    @Query("SELECT * FROM AlarmTaskEntity")
    fun selectAll(): Single<List<AlarmTaskEntity>>
}