package com.carterchen247.alarmscheduler.demo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.carterchen247.alarmscheduler.demo.log.ListItem
import com.carterchen247.alarmscheduler.demo.log.MessageDispatcher
import com.carterchen247.alarmscheduler.extension.openExactAlarmSettingPage
import java.time.LocalDateTime

class MainActivity : AbstractListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<View>(R.id.btnSchedule).setOnClickListener {
            presenter.scheduleAlarm()
        }

        findViewById<View>(R.id.btnGetScheduledAlarmsInfo).setOnClickListener {
            presenter.requestScheduledAlarmsInfo()
        }

        MessageDispatcher.subscribeMessage { msg ->
            val now = LocalDateTime.now()
            addListItem(ListItem(msg, now.toString()))
        }
    }

    override fun showExactAlarmPermissionSetupDialog() {
        AlertDialog.Builder(this)
            .setTitle("Cannot schedule")
            .setMessage("Exact alarm permission is needed to schedule an alarm")
            .setPositiveButton("setup") { _, _ ->
                openExactAlarmSettingPage()
            }
            .setNegativeButton("cancel") { _, _ -> }
            .create()
            .show()
    }
}
