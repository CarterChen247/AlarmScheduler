package com.carterchen247.alarmscheduler.demo

import com.carterchen247.alarmscheduler.demo.log.EventBus
import com.carterchen247.alarmscheduler.model.DataPayload
import com.carterchen247.alarmscheduler.task.AlarmTask
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class DemoAlarmTask : AlarmTask {

    companion object {
        const val TYPE = 1
    }

    override fun onAlarmFires(alarmId: Int, dataPayload: DataPayload) {
        val msg = """
            onAlarmFires callback was triggered.
            alarmId=$alarmId
            dataPayload=
        """.trimIndent()

        val dataPayloadString = GsonBuilder().setPrettyPrinting().create().toJson(dataPayload)
        EventBus.dispatchMessage(msg + dataPayloadString)
    }
}
