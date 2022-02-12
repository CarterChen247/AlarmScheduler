package com.carterchen247.alarmscheduler.demo

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carterchen247.alarmscheduler.AlarmScheduler
import com.carterchen247.alarmscheduler.demo.log.LogItem
import com.carterchen247.alarmscheduler.demo.log.LogItemAdapter
import com.carterchen247.alarmscheduler.demo.log.LogObservable
import com.carterchen247.alarmscheduler.model.AlarmConfig
import com.carterchen247.alarmscheduler.model.AlarmInfo
import com.carterchen247.alarmscheduler.model.DataPayload
import com.carterchen247.alarmscheduler.model.ScheduledAlarmsCallback
import java.time.LocalDateTime
import kotlin.math.max

class MainActivity : AppCompatActivity() {

    private val logItemAdapter = LogItemAdapter()
    private lateinit var logList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initLogList()

        findViewById<View>(R.id.btnSchedule).setOnClickListener {
            scheduleDemoAlarm()
        }

        findViewById<View>(R.id.btnGetScheduledAlarmsInfo).setOnClickListener {
            requestScheduledAlarmsInfo()
        }
    }

    private fun requestScheduledAlarmsInfo() {
        AlarmScheduler.getScheduledAlarmsAsync(object : ScheduledAlarmsCallback {
            override fun onResult(scheduledAlarms: List<AlarmInfo>) {
                val msg = "scheduled alarms=$scheduledAlarms"
                val now = LocalDateTime.now()
                addLogItem(LogItem(msg, now.toString()))
            }
        })
    }

    private fun scheduleDemoAlarm() {
        AlarmConfig(System.currentTimeMillis() + 10000L, DemoAlarmTask.TYPE) {
            dataPayload(DataPayload().apply { putString("reminder", "have a meeting") })
        }.schedule()
    }

    private fun initLogList() {
        logList = findViewById(R.id.logList)
        logList.run {
            addItemDecoration(createItemDecoration())
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = logItemAdapter
        }
        LogObservable.setObserver { msg ->
            val now = LocalDateTime.now()
            addLogItem(LogItem(msg, now.toString()))
        }
    }

    private fun addLogItem(item: LogItem) {
        runOnUiThread {
            logItemAdapter.addItem(item)
            scrollToBottom()
        }
    }

    private fun scrollToBottom() {
        logList.scrollToPosition(max(0, logItemAdapter.itemCount - 1))
    }

    private fun createItemDecoration(): RecyclerView.ItemDecoration {
        return object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                val dividerHeight = resources.getDimension(R.dimen.divider_height).toInt()
                val position = parent.getChildLayoutPosition(view)
                if (position == 0) {
                    outRect.top = dividerHeight
                }
                outRect.bottom = dividerHeight
            }
        }
    }
}
