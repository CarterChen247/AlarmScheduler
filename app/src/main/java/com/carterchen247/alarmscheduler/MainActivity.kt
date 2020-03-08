package com.carterchen247.alarmscheduler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // schedule an alarm
        val setting = AlarmConfig(DemoAlarmTask.TYPE, 1000L).apply {
            alarmId = 1
            customData = null
        }
        AlarmScheduler.schedule(setting)
    }
}
