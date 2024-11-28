package com.carterchen247.alarmscheduler

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carterchen247.alarmscheduler.demo.MainPresenter
import com.carterchen247.alarmscheduler.demo.MainView
import com.carterchen247.alarmscheduler.demo.R
import com.carterchen247.alarmscheduler.demo.log.ListItem
import com.carterchen247.alarmscheduler.demo.log.ListItemAdapter
import kotlin.math.max

abstract class AbstractListActivity : AppCompatActivity(), MainView {

    protected val presenter by lazy { MainPresenter(this) }
    private val listItemAdapter by lazy { ListItemAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListView()
        presenter.init()
    }

    private fun initListView() {
        findViewById<RecyclerView>(R.id.listView).run {
            addItemDecoration(createItemDecoration())
            layoutManager = LinearLayoutManager(this@AbstractListActivity)
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
        findViewById<RecyclerView>(R.id.listView).scrollToPosition(max(0, listItemAdapter.itemCount - 1))
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

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }
}