package com.example.c232finalproject.consistencyiskey

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainRecyclerViewAdapter(
    private val days: List<DayContainer>,
    private val context: Context
) : RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val dayLabel: TextView = view.findViewById(R.id.day_label)
        val imageView: ImageView = view.findViewById(R.id.image_view)
        val rootView: LinearLayout = view as LinearLayout
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.day_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        ("Day ${days[position]}").also { viewHolder.dayLabel.text = it }
        if(!days[position].completed) {
            viewHolder.imageView.visibility = View.INVISIBLE
        }
        viewHolder.rootView.setOnClickListener {
            val i: Intent = Intent(context, TodoActivity::class.java).apply {
                putExtra("key", days[position].index)
            }
            context.startActivity(i)
        }
    }

    override fun getItemCount() = days.size

}
