package com.carterchen247.alarmscheduler.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.carterchen247.alarmscheduler.AlarmScheduler
import com.carterchen247.alarmscheduler.demo.log.LogAdapter
import com.carterchen247.alarmscheduler.demo.log.LogItem
import com.carterchen247.alarmscheduler.demo.log.LogObservable
import com.carterchen247.alarmscheduler.model.AlarmConfig
import com.carterchen247.alarmscheduler.model.AlarmInfo
import com.carterchen247.alarmscheduler.model.DataPayload
import com.carterchen247.alarmscheduler.model.ScheduledAlarmsCallback
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val listAdapter = LogAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initLogList()

        // schedule an alarm
        btnSchedule.setOnClickListener {
            AlarmConfig(System.currentTimeMillis() + 10000L, DemoAlarmTask.TYPE) {
                dataPayload(DataPayload().apply { putString("reminder", "have a meeting") })
            }.schedule()
        }

        btnGetScheduledTaskCount.setOnClickListener {
            AlarmScheduler.getScheduledAlarmsAsync(object : ScheduledAlarmsCallback {
                override fun onResult(scheduledAlarms: List<AlarmInfo>) {
                    val msg = "scheduled alarms=$scheduledAlarms"
                    Timber.d(msg)
                    runOnUiThread {
                        listAdapter.addItem(LogItem(msg))
                    }
                }
            })
        }
    }

    private fun initLogList() {
        logList.run {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, true)
            adapter = listAdapter
        }
        LogObservable.setObserver { msg ->
            runOnUiThread {
                listAdapter.addItem(LogItem(msg))
            }
        }
    }
}
