package com.carterchen247.alarmscheduler.model

import com.carterchen247.alarmscheduler.AlarmScheduler
import com.carterchen247.alarmscheduler.constant.AlarmID

class AlarmConfig(
    private val type: Int,
    private val triggerTime: Long,
    block: (AlarmConfig.() -> Unit)? = null
) {
    private var alarmId: Int = AlarmID.AUTO_GENERATE
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

    fun schedule() {
        AlarmScheduler.getInstance().schedule(getInfo())
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