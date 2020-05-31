package com.carterchen247.alarmscheduler.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.carterchen247.alarmscheduler.AlarmScheduler
import com.carterchen247.alarmscheduler.model.AlarmConfig
import com.carterchen247.alarmscheduler.model.AlarmTaskCountCallback
import com.carterchen247.alarmscheduler.model.DataPayload
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSchedule.setOnClickListener {
            Timber.d("schedule an alarm")
            AlarmConfig(System.currentTimeMillis() + 10000L, DemoAlarmTask.TYPE) {
                dataPayload(DataPayload().apply { putString("reminder", "have a meeting") })
            }.schedule()
        }

        btnGetScheduledTaskCount.setOnClickListener {
            AlarmScheduler.getScheduledAlarmTaskCountAsync(object : AlarmTaskCountCallback {
                override fun onResult(count: Int) {
                    Timber.d("scheduled alarm task count=$count")
                }
            })
        }
    }
}
