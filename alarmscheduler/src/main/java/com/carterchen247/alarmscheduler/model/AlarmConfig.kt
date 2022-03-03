package com.carterchen247.alarmscheduler.model

import com.carterchen247.alarmscheduler.AlarmIdProvider

class AlarmConfig(
    private val triggerTime: Long,
    private val type: Int = 0,
    block: (AlarmConfig.() -> Unit)? = null
) {
    private var alarmId: Int = AlarmIdProvider.AUTO_GENERATE
    private var dataPayload: DataPayload? = null

    init {
        apply { block?.invoke(this) }
    }

    fun id(alarmId: Int) {
        this.alarmId = alarmId
    }

    fun dataPayload(dataPayload: DataPayload) {
        this.dataPayload = dataPayload
    }

    fun dataPayload(vararg pairs: Pair<String, Any>) {
        dataPayload(DataPayload.of(*pairs))
    }

    internal fun getInfo(): AlarmInfo {
        return AlarmInfo(
            type,
            triggerTime,
            alarmId,
            dataPayload
        )
    }
}