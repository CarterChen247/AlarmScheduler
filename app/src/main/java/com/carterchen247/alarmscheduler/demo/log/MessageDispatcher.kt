package com.carterchen247.alarmscheduler.demo.log

object MessageDispatcher {
    private var observer: ((String) -> Unit)? = null

    fun dispatchMessage(msg: String) {
        observer?.invoke(msg)
    }

    fun subscribeMessage(observer: ((String) -> Unit)) {
        this.observer = observer
    }
}