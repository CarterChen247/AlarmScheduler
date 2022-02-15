package com.carterchen247.alarmscheduler.storage.converter

import androidx.room.TypeConverter
import com.carterchen247.alarmscheduler.model.DataPayload

internal class DataPayloadTypeConverter {

    private val adapter = DataPayloadJsonFormatAdapter()

    @TypeConverter
    fun stringToDataPayload(string: String): DataPayload {
        return adapter.fromJson(string)
    }

    @TypeConverter
    fun dataPayloadToString(dataPayload: DataPayload?): String {
        return adapter.toJson(dataPayload)
    }
}