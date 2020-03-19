package com.carterchen247.alarmscheduler

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.carterchen247.alarmscheduler.storage.AlarmSchedulerDatabase
import com.carterchen247.alarmscheduler.storage.AlarmStateDao
import com.carterchen247.alarmscheduler.storage.AlarmStateEntity
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomAlarmStateEntityTest {
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
    fun insertId() {
        dao.insertEntity(AlarmStateEntity(id = 1))
            .subscribeOn(Schedulers.trampoline())
            .subscribe()
        dao.insertEntity(AlarmStateEntity(id = 2))
            .subscribeOn(Schedulers.trampoline())
            .subscribe()
        dao.selectAll()
            .subscribeOn(Schedulers.trampoline())
            .test()
            .assertValue {
                it.size == 2
            }
    }

    /**
     * if not use @Insert(onConflict = OnConflictStrategy.REPLACE) in AlarmStateDao#insertEntity
     * will receive following exception:
     * android.database.sqlite.SQLiteConstraintException: UNIQUE constraint failed: AlarmStateEntity.id
     */
    @Test
    fun replaceIdWhenIdConflicts() {
        dao.insertEntity(AlarmStateEntity(id = 1))
            .subscribeOn(Schedulers.trampoline())
            .subscribe()
        dao.insertEntity(AlarmStateEntity(id = 1))
            .subscribeOn(Schedulers.trampoline())
            .subscribe()
        dao.selectAll()
            .subscribeOn(Schedulers.trampoline())
            .test()
            .assertValue {
                it.size == 1
            }
    }
}