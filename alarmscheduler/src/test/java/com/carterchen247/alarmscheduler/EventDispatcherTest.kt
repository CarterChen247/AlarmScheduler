package com.carterchen247.alarmscheduler

import com.carterchen247.alarmscheduler.event.AlarmSchedulerEventObserver
import com.carterchen247.alarmscheduler.event.EventDispatcher
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch

class EventDispatcherTest {

    private lateinit var countDownLatch: CountDownLatch
    private var reThrowException: Throwable? = null

    @Before
    fun setUp() {
        EventDispatcher.reset()
        countDownLatch = CountDownLatch(2)
    }

    @Test
    fun `concurrent modification test`() {
        repeat(1000) {
            val observer = AlarmSchedulerEventObserver {}
            EventDispatcher.addEventObserver(observer)
        }

        val thread1 = createRemovingObserverWorkerThread().apply { name = "thread1" }
        val thread2 = createRemovingObserverWorkerThread().apply { name = "thread2" }
        thread1.start()
        thread2.start()
        countDownLatch.await()

        reThrowException?.let { exception ->
            throw exception
        }
    }

    private fun createRemovingObserverWorkerThread() = Thread {
        try {
            EventDispatcher.peekObservers().forEach { observer ->
                EventDispatcher.removeObserver(observer)
            }
        } catch (exception: Throwable) {
            reThrowException = exception
        } finally {
            countDownLatch.countDown()
        }
    }
}