package com.carterchen247.alarmscheduler.model

data class DataPayload(
    val dataMap: Map<String, Any?>
) {
    companion object {
        fun of(map: Map<String, Any?>) = DataPayload(map)

        fun of(vararg pairs: Pair<String, Any?>) = of(mapOf(*pairs))
    }
}