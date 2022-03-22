package com.carterchen247.alarmscheduler.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [AlarmStateEntity::class],
    version = 1
)
internal abstract class AlarmSchedulerDatabase : RoomDatabase() {

    abstract fun getAlarmStateDao(): AlarmStateDao

    companion object {
        private const val DB_NAME = "AlarmScheduler"

        fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                AlarmSchedulerDatabase::class.java,
                DB_NAME,
            ).build()
    }
}