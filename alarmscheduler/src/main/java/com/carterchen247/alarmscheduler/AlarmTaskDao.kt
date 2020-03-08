package com.carterchen247.alarmscheduler

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import io.reactivex.Single

@Dao
interface AlarmTaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEntity(entity: AlarmTaskEntity): Single<Long>
}