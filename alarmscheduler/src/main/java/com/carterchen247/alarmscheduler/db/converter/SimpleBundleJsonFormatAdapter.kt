package com.carterchen247.alarmscheduler.db.converter

import android.os.Bundle
import org.json.JSONObject

class SimpleBundleJsonFormatAdapter : BundleJsonFormatAdapter {

    override fun toJson(bundle: Bundle): String {
        val jsonObject = JSONObject()
        if (bundle.isEmpty) {
            return jsonObject.toString()
        }
        for (key in bundle.keySet()) {
            val value = bundle.get(key)
            jsonObject.put(key, value)
        }
        return jsonObject.toString()
    }

    override fun fromJson(json: String): Bundle {
        val bundle = Bundle()
        if (json.isEmpty()) {
            return bundle
        }
        val jsonObject = JSONObject(json)
        if (jsonObject.length() == 0) {
            return bundle
        }
        return bundle.apply {
            for (key in jsonObject.keys()) {
                when (val value = jsonObject.get(key)) {
                    is String -> putString(key, value)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is Boolean -> putBoolean(key, value)
                }
            }
        }
    }
}