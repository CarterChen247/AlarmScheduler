package com.carterchen247.alarmscheduler

class AlarmConfig(
    private val type: Int,
    private val triggerTime: Long,
    block: (AlarmConfig.() -> Unit)? = null
) {
    private var alarmId: Int = Constant.AUTO_GENERATE
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

    internal fun getInfo(): AlarmInfo {
        return AlarmInfo(
            type,
            triggerTime,
            alarmId,
            dataPayload
        )
    }
}