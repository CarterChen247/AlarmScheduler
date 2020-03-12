package com.carterchen247.alarmscheduler.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.carterchen247.alarmscheduler.AlarmInfo
import com.carterchen247.alarmscheduler.AlarmScheduler
import com.carterchen247.alarmscheduler.DataPayload
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSchedule.setOnClickListener {
            // schedule an alarm
            val config = AlarmInfo(
                alarmType = DemoAlarmTask.TYPE,
                triggerTime = System.currentTimeMillis() + 5000L,
                dataPayload = DataPayload().apply { putString("reminder", "have a meeting") }
            )
            AlarmScheduler.schedule(config)
        }
    }
}
