package com.example.c232finalproject.consistencyiskey

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class TodoRecyclerViewAdapter(
    private val tasks: List<Task>,
    private val context: Context
) : RecyclerView.Adapter<TodoRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descLabel: TextView = view.findViewById(R.id.day_label)
        val imageView: ImageView = view.findViewById(R.id.image_view)
        val rootView: ConstraintLayout = view as ConstraintLayout
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.day_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.descLabel.text = tasks[position].name
        if(!tasks[position].completed) {
            viewHolder.imageView.visibility = View.INVISIBLE
        }
        viewHolder.rootView.setOnClickListener {
            //todo implement
        }
    }

    override fun getItemCount() = tasks.size

}