package com.carterchen247.alarmscheduler.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.carterchen247.alarmscheduler.AlarmConfig
import com.carterchen247.alarmscheduler.AlarmScheduler

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // schedule an alarm
        val config = AlarmConfig(DemoAlarmTask.TYPE, System.currentTimeMillis() + 5000L).apply {
            //            alarmId = 1
            customData = Bundle()
        }
        AlarmScheduler.schedule(config)

//        Handler().postDelayed(Runnable {
//            AlarmScheduler.cancel(1)
//        }, 2000L)
//        Handler().postDelayed(Runnable {
//            AlarmScheduler.cancel(1)
//        }, 3000L)
    }
}
