package com.carterchen247.alarmscheduler.demo.log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carterchen247.alarmscheduler.demo.R

class ListItemAdapter : RecyclerView.Adapter<ListItemAdapter.ViewHolder>() {

    private val items = mutableListOf<ListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun addItem(item: ListItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: ListItem) {
            itemView.findViewById<TextView>(R.id.tvLog).text = item.msg
            itemView.findViewById<TextView>(R.id.tvTime).text = item.time
        }
    }
}