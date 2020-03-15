package com.carterchen247.alarmscheduler.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [AlarmTaskEntity::class],
    version = 1
)
abstract class AlarmTaskDatabase : RoomDatabase() {

    abstract fun getAlarmTaskDao(): AlarmTaskDao

    companion object {
        private const val DB_NAME = "AlarmScheduler"

        @Volatile
        private var INSTANCE: AlarmTaskDatabase? = null

        fun getInstance(context: Context): AlarmTaskDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AlarmTaskDatabase::class.java, DB_NAME
            ).build()
    }
}