package com.carterchen247.alarmscheduler.storage.converter

import android.os.Bundle

internal interface BundleJsonFormatAdapter {
    fun toJson(bundle: Bundle): String
    fun fromJson(json: String): Bundle
}
