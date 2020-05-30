package com.carterchen247.alarmscheduler

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.carterchen247.alarmscheduler.storage.AlarmSchedulerDatabase
import com.carterchen247.alarmscheduler.storage.AlarmStateDao
import com.carterchen247.alarmscheduler.storage.AlarmStateEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class RoomAlarmStateEntityTest {
    private lateinit var db: AlarmSchedulerDatabase
    private lateinit var dao: AlarmStateDao

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AlarmSchedulerDatabase::class.java
        ).build()
        dao = db.getAlarmStateDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertId() = runBlocking {
        dao.insertEntity(AlarmStateEntity(id = 1))
        dao.insertEntity(AlarmStateEntity(id = 2))
        val items = dao.selectAll()
        assertEquals(2, items.size)
    }

    /**
     * if not use @Insert(onConflict = OnConflictStrategy.REPLACE) in AlarmStateDao#insertEntity
     * will receive following exception:
     * android.database.sqlite.SQLiteConstraintException: UNIQUE constraint failed: AlarmStateEntity.id
     */
    @Test
    fun replaceIdWhenIdConflicts() = runBlocking {
        dao.insertEntity(AlarmStateEntity(id = 1))
        dao.insertEntity(AlarmStateEntity(id = 1))
        val items = dao.selectAll()
        assertEquals(1, items.size)
    }
}