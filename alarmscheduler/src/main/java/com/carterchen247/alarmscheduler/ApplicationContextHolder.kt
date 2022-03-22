package com.carterchen247.alarmscheduler

import android.annotation.SuppressLint
import android.content.Context

/**
 * A helper class to keep reference of [Context.getApplicationContext]
 */
@SuppressLint("StaticFieldLeak")
object ApplicationContextHolder {
    private lateinit var applicationContext: Context

    fun setInstance(context: Context) {
        applicationContext = context.applicationContext
    }

    fun getInstance() = applicationContext
}