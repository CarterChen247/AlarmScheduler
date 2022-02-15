package com.carterchen247.alarmscheduler.storage.converter

import com.carterchen247.alarmscheduler.model.DataPayload
import org.json.JSONObject

internal class DataPayloadJsonFormatAdapter {

    fun toJson(dataPayload: DataPayload?): String {
        val dataMap = dataPayload?.dataMap.orEmpty()
        val jsonObject = JSONObject(dataMap)
        return jsonObject.toString()
    }

    fun fromJson(json: String): DataPayload {
        if (json.isEmpty()) {
            return DataPayload.of(emptyMap())
        }
        val hashMap = HashMap<String, Any?>()
        val jsonObject = JSONObject(json)
        jsonObject.keys().forEach { key ->
            val value = jsonObject.get(key)
            hashMap[key] = value
        }
        return DataPayload.of(hashMap)
    }
}