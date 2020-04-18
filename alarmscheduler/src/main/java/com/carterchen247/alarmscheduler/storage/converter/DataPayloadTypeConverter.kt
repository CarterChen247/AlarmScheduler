package com.carterchen247.alarmscheduler.storage.converter

import android.os.Bundle
import androidx.room.TypeConverter
import com.carterchen247.alarmscheduler.model.DataPayload

internal class DataPayloadTypeConverter {

    private val adapter: BundleJsonFormatAdapter = SimpleBundleJsonFormatAdapter()

    @TypeConverter
    fun stringToDataPayload(string: String): DataPayload {
        return DataPayload.create(adapter.fromJson(string))
    }

    @TypeConverter
    fun dataPayloadToString(dataPayload: DataPayload?): String {
        return adapter.toJson(dataPayload?.getBundle() ?: Bundle())
    }
}