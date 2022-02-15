package com.carterchen247.alarmscheduler.extension

import android.os.Bundle
import androidx.core.os.bundleOf

fun Bundle?.toMap(): Map<String, Any?> {
    if (this == null) {
        return emptyMap()
    }
    val hashMap = HashMap<String, Any?>()
    keySet().forEach { key ->
        hashMap[key] = get(key)
    }
    return hashMap
}

fun Map<String, Any?>.toBundle() = bundleOf(*this.toList().toTypedArray())