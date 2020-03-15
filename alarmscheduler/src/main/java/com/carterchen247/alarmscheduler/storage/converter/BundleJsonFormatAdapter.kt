package com.carterchen247.alarmscheduler.storage.converter

import android.os.Bundle

interface BundleJsonFormatAdapter {
    fun toJson(bundle: Bundle): String
    fun fromJson(json: String): Bundle
}
