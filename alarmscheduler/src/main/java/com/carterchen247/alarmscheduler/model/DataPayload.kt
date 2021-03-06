package com.carterchen247.alarmscheduler.model

import android.os.Bundle

class DataPayload {

    private var bundle = Bundle()

    fun putInt(key: String, value: Int) {
        bundle.putInt(key, value)
    }

    fun getInt(key: String): Int {
        return bundle.getInt(key)
    }

    fun putString(key: String, value: String) {
        bundle.putString(key, value)
    }

    fun getString(key: String): String? {
        return bundle.getString(key)
    }

    fun putBoolean(key: String, value: Boolean) {
        bundle.putBoolean(key, value)
    }

    fun getBoolean(key: String): Boolean {
        return bundle.getBoolean(key)
    }

    fun size(): Int {
        return bundle.size()
    }

    fun keySet(): Set<String> {
        return bundle.keySet()
    }

    internal fun getBundle(): Bundle {
        return bundle
    }

    internal fun setBundle(bundle: Bundle) {
        this.bundle = bundle
    }

    companion object {
        internal fun create(bundle: Bundle?): DataPayload {
            if (bundle == null) {
                return DataPayload()
            }
            return DataPayload().apply {
                setBundle(bundle)
            }
        }
    }
}