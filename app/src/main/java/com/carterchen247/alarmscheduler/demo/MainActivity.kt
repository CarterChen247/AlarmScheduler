package com.carterchen247.alarmscheduler.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.carterchen247.alarmscheduler.AlarmConfig
import com.carterchen247.alarmscheduler.DataPayload
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSchedule.setOnClickListener {
            Timber.d("schedule an alarm")

            // schedule an alarm
            AlarmConfig(DemoAlarmTask.TYPE, System.currentTimeMillis() + 10000L) {
                dataPayload(DataPayload().apply { putString("reminder", "have a meeting") })
            }.schedule()
        }
    }
}
