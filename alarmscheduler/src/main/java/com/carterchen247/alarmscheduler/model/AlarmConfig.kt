package com.carterchen247.alarmscheduler.model

import com.carterchen247.alarmscheduler.AlarmIdProvider
import com.carterchen247.alarmscheduler.AlarmScheduler

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

    /**
     * This function is deprecated. Please use [com.carterchen247.alarmscheduler.extension.scheduleAlarm] instead.
     */
    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("this function is deprecated.")
    fun schedule(): Int {
        return AlarmScheduler.getImpl().schedule(getInfo())
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