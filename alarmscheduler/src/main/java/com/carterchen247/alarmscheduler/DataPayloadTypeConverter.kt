package com.carterchen247.alarmscheduler

import androidx.room.TypeConverter

class DataPayloadTypeConverter {

    private val adapter: BundleJsonFormatAdapter = SimpleBundleJsonFormatAdapter()

    @TypeConverter
    fun stringToDataPayload(string: String): DataPayload {
        return DataPayload.create(adapter.fromJson(string))
    }

    @TypeConverter
    fun dataPayloadToString(dataPayload: DataPayload): String {
        return adapter.toJson(dataPayload.getBundle())
    }
}