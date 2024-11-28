package com.carterchen247.alarmscheduler.demo

import com.google.gson.GsonBuilder

object PrettyFormatter {

    private val gson by lazy { GsonBuilder().setPrettyPrinting().create() }

    fun format(obj: Any): String {
        return gson.toJson(obj)
    }
}