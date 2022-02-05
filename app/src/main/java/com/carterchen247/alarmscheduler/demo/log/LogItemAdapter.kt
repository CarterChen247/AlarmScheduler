package com.carterchen247.alarmscheduler.demo.log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.carterchen247.alarmscheduler.demo.R
import kotlinx.android.synthetic.main.item_log.view.*

class LogItemAdapter : RecyclerView.Adapter<LogItemAdapter.ViewHolder>() {

    private val items = mutableListOf<LogItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_log, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun addItem(item: LogItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: LogItem) {
            itemView.tvLog.text = item.msg
            itemView.tvTime.text = item.time
        }
    }
}