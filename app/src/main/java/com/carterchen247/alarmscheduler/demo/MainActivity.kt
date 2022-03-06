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
import com.carterchen247.alarmscheduler.event.AlarmSchedulerEventObserver
import com.carterchen247.alarmscheduler.event.ScheduleExactAlarmPermissionGrantedEvent
import com.carterchen247.alarmscheduler.model.AlarmConfig
import com.carterchen247.alarmscheduler.model.AlarmInfo
import com.carterchen247.alarmscheduler.model.ScheduleResult
import com.carterchen247.alarmscheduler.model.ScheduledAlarmsCallback
import java.time.LocalDateTime
import java.util.*
import kotlin.math.max

class MainActivity : AppCompatActivity() {

    private val logItemAdapter = LogItemAdapter()
    private lateinit var logList: RecyclerView
    private val alarmSchedulerEventObserver = createAlarmSchedulerEventObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initLogList()
        AlarmScheduler.addEventObserver(alarmSchedulerEventObserver)

        findViewById<View>(R.id.btnSchedule).setOnClickListener {
            val config = AlarmConfig(
                Date().time + 10000L,
                DemoAlarmTask.TYPE
            ) {
                dataPayload("reminder" to "have a meeting")
            }
            AlarmScheduler.schedule(config) { result ->
                // result callback is optional
                when (result) {
                    is ScheduleResult.Success -> {
                        val now = LocalDateTime.now()
                        addLogItem(LogItem("The id of the scheduled alarm = ${result.alarmId}", now.toString()))
                    }
                    is ScheduleResult.Failure -> {
                        val now = LocalDateTime.now()
                        addLogItem(LogItem("Alarm scheduling is failed, exception = ${result.exception}", now.toString()))
                    }
                }
            }
        }

        findViewById<View>(R.id.btnGetScheduledAlarmsInfo).setOnClickListener {
            requestScheduledAlarmsInfo()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AlarmScheduler.removeEventObserver(alarmSchedulerEventObserver)
    }

    private fun createAlarmSchedulerEventObserver() = AlarmSchedulerEventObserver { event ->
        if (event is ScheduleExactAlarmPermissionGrantedEvent) {
            val now = LocalDateTime.now()
            addLogItem(LogItem("The permission to schedule exact alarms has been granted", now.toString()))
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
