package com.carterchen247.alarmscheduler.storage

import androidx.room.*

@Dao
internal interface AlarmStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntity(entity: AlarmStateEntity): Long

    @Delete
    suspend fun removeEntity(alarmStateEntity: AlarmStateEntity)

    @Query("SELECT * FROM AlarmStateEntity")
    suspend fun selectAll(): List<AlarmStateEntity>
}