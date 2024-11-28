package com.carterchen247.alarmscheduler.demo

import com.carterchen247.alarmscheduler.demo.log.MessageDispatcher
import com.carterchen247.alarmscheduler.model.DataPayload
import com.carterchen247.alarmscheduler.task.AlarmTask

class DemoAlarmTask : AlarmTask {

    companion object {
        const val TYPE = 1
    }

    override fun onAlarmFires(alarmId: Int, dataPayload: DataPayload) {
        val map = buildMap {
            put("alarmId", alarmId)
            put("alarmType", TYPE)
            put("dataPayload", dataPayload)
        }
        val msg = """Alarm triggered = ${PrettyFormatter.format(map)}""".trimIndent()
        MessageDispatcher.dispatchMessage(msg)
    }
}
