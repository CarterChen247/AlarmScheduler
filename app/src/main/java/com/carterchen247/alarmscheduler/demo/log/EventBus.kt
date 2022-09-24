package com.carterchen247.alarmscheduler.demo.log

object EventBus {
    private var observer: ((String) -> Unit)? = null

    fun dispatchMessage(msg: String) {
        observer?.invoke(msg)
    }

    fun setObserver(observer: ((String) -> Unit)) {
        this.observer = observer
    }
}