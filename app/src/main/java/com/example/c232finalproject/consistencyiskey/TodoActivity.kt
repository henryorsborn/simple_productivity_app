package com.example.c232finalproject.consistencyiskey

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

class TodoActivity : AppCompatActivity() {

    var index: Int? = null
    var data: DataContainer? = null
    var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)
        recyclerView = findViewById(R.id.todo_recycler)
        findViewById<FloatingActionButton>(R.id.addButton).setOnClickListener {
            addTask()
        }
        index = intent.extras?.getInt("key")
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = TodoRecyclerViewAdapter(loadData(),this)
    }

    private fun loadData(): List<Task>{
        var fileInputStream: FileInputStream? = null
        fileInputStream = openFileInput("base.json")
        val inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
        val stringBuilder: StringBuilder = StringBuilder()
        var text: String? = null
        while ({ text = bufferedReader.readLine(); text }() != null) {
            stringBuilder.append(text)
        }
        val mapper = jacksonObjectMapper()
        val container = mapper.readValue(stringBuilder.toString(),DataContainer::class.java)
        data = container
        return container.data[index ?: 0].tasks
    }

    private fun addTask() {
        val dialogBuilder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.create_task_dialog, null)
        dialogBuilder
            .setCancelable(true)
            .setView(view)
        val dialog = dialogBuilder.create()
        view.findViewById<Button>(R.id.todo_dialog_cancel_button).setOnClickListener {
            dialog.cancel()
        }
        view.findViewById<Button>(R.id.todo_dialog_ok_button).setOnClickListener {
            var tasks: List<Task>? = data?.data?.get(index ?: 0)?.tasks
            val mutableTasks: MutableList<Task>? = tasks?.toMutableList()
            mutableTasks?.add(Task(view.findViewById<EditText>(R.id.desc_input).text.toString(),false))
            tasks = mutableTasks?.toList()
            val dayContainer = tasks?.let { it1 -> index?.let { it2 -> DayContainer(it2, false, it1) } }
            val dayContainers: MutableList<DayContainer> = data?.data?.toMutableList() ?: mutableListOf()
            dayContainers[index ?: 0] = dayContainer ?: DayContainer(0,false, listOf())
            data = DataContainer(dayContainers)
            recyclerView?.adapter = TodoRecyclerViewAdapter(data?.data?.get(index ?: 0)?.tasks ?: listOf(),this)
        }
        dialog.show()
    }

    private fun saveData() {
        //todo implement
    }

}