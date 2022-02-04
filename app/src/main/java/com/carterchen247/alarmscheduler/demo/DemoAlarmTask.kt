package com.carterchen247.alarmscheduler.demo

import com.carterchen247.alarmscheduler.demo.log.LogObservable
import com.carterchen247.alarmscheduler.model.DataPayload
import com.carterchen247.alarmscheduler.task.AlarmTask

class DemoAlarmTask : AlarmTask {

    companion object {
        const val TYPE = 1
    }

    override fun onAlarmFires(alarmId: Int, dataPayload: DataPayload) {
        val msg = """
            The callback of the scheduled alarm triggered.
            alarmId=$alarmId, dataPayload.keySet=${dataPayload.keySet().toList()}
        """.trimIndent()
        LogObservable.dispatchMessage(msg)
    }
}
