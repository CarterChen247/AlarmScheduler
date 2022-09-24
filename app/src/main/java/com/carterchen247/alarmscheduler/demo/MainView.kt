package com.carterchen247.alarmscheduler.demo

import com.carterchen247.alarmscheduler.demo.log.ListItem

interface MainView {
    fun showExactAlarmPermissionSetupDialog()
    fun addListItem(item: ListItem)
}