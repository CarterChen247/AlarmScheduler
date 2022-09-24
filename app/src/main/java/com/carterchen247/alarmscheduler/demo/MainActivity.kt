package com.carterchen247.alarmscheduler.demo

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carterchen247.alarmscheduler.demo.databinding.ActivityMainBinding
import com.carterchen247.alarmscheduler.demo.log.EventBus
import com.carterchen247.alarmscheduler.demo.log.ListItem
import com.carterchen247.alarmscheduler.demo.log.ListItemAdapter
import com.carterchen247.alarmscheduler.extension.openExactAlarmSettingPage
import java.time.LocalDateTime
import kotlin.math.max

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var binding: ActivityMainBinding
    private val presenter by lazy { MainPresenter(this) }
    private val listItemAdapter by lazy { ListItemAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListView()
        presenter.init()

        binding.btnSchedule.setOnClickListener {
            presenter.scheduleAlarm()
        }

        binding.btnGetScheduledAlarmsInfo.setOnClickListener {
            presenter.requestScheduledAlarmsInfo()
        }

        EventBus.setObserver { msg ->
            val now = LocalDateTime.now()
            addListItem(ListItem(msg, now.toString()))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
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

    private fun initListView() {
        binding.listView.run {
            addItemDecoration(createItemDecoration())
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listItemAdapter
        }
    }

    override fun addListItem(item: ListItem) {
        runOnUiThread {
            listItemAdapter.addItem(item)
            scrollToBottom()
        }
    }

    private fun scrollToBottom() {
        binding.listView.scrollToPosition(max(0, listItemAdapter.itemCount - 1))
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
