package com.carterchen247.alarmscheduler.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [AlarmStateEntity::class],
    version = 1
)
abstract class AlarmSchedulerDatabase : RoomDatabase() {

    abstract fun getAlarmStateDao(): AlarmStateDao

    companion object {
        private const val DB_NAME = "AlarmScheduler"

        @Volatile
        private var INSTANCE: AlarmSchedulerDatabase? = null

        fun getInstance(context: Context): AlarmSchedulerDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AlarmSchedulerDatabase::class.java, DB_NAME
            ).build()
    }
}