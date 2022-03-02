package com.carterchen247.alarmscheduler.event

import androidx.annotation.VisibleForTesting
import java.util.concurrent.CopyOnWriteArrayList

internal object EventDispatcher {
    private val observers = CopyOnWriteArrayList<AlarmSchedulerEventObserver>()

    fun addEventObserver(observer: AlarmSchedulerEventObserver) {
        observers.add(observer)
    }

    fun removeObserver(observer: AlarmSchedulerEventObserver) {
        observers.remove(observer)
    }

    fun dispatchEvent(event: AlarmSchedulerEvent) {
        observers.forEach { observer ->
            observer.onEventDispatched(event)
        }
    }

    @VisibleForTesting
    fun reset() {
        observers.clear()
    }

    @VisibleForTesting
    fun peekObservers() = observers
}