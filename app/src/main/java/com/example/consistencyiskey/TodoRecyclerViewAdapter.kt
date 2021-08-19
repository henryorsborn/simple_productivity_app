package com.example.c232finalproject.consistencyiskey

import android.content.Context
import android.content.DialogInterface
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class TodoRecyclerViewAdapter(
    private val tasks: List<Task>,
    private val context: Context
) : RecyclerView.Adapter<TodoRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val descLabel: TextView = view.findViewById(R.id.desc_label)
        val imageOn: ImageView = view.findViewById(R.id.image_on)
        val rootView: ConstraintLayout = view as ConstraintLayout
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.todo_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.descLabel.text = tasks[position].name
        if(tasks[position].completed) {
            viewHolder.imageOn.visibility = View.VISIBLE
        } else {
            viewHolder.imageOn.visibility = View.INVISIBLE
        }
        viewHolder.rootView.setOnClickListener {
            if(!tasks[position].completed) {
                val dialogBuilder = AlertDialog.Builder(context)
                dialogBuilder
                    .setCancelable(true)
                    .setMessage("Would you like to mark this task as complete")
                    .setPositiveButton("Yes") { dialog, _ ->
                        viewHolder.imageOn.visibility = View.VISIBLE
                        (context as TodoActivity).completeTask(position)
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.cancel()
                    }
                val dialog = dialogBuilder.create()
                dialog.show()
            }
        }
        val gestureListener: GestureDetector.SimpleOnGestureListener = object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (velocityX > 0) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                    builder.setTitle("Do you want to delete this item")
                    builder.setNegativeButton("No") { dialogInterface, _ -> dialogInterface.cancel() }
                    builder.setPositiveButton("Yes") { _, _ -> (context as TodoActivity).deleteTask(position) }
                    builder.setCancelable(false)
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.show()
                }
                return true
            }
        }
        val gd = GestureDetector(context, gestureListener)
        viewHolder.rootView.setOnTouchListener { v, event -> gd.onTouchEvent(event) }
    }

    override fun getItemCount() = tasks.size

}