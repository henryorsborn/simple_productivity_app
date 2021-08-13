package com.example.c232finalproject.consistencyiskey

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class TodoActivity : AppCompatActivity() {

    private var index: Int? = null
    private var data: DataContainer? = null
    private var recyclerView: RecyclerView? = null
    private var completeButton: Button? = null

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
        completeButton = findViewById(R.id.complete_button)
        completeButton?.setOnClickListener {
            completeDay()
        }
        checkCompletedDay()
    }

    private fun loadData(): List<Task>{
        val fileInputStream = openFileInput("preferences.json")
        val inputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        val stringBuilder: StringBuilder = StringBuilder()
        var text: String?
        while (run {
                text = bufferedReader.readLine()
                text
            } != null) {
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
            dayContainers[index ?: 0] = dayContainer ?: DayContainer(index ?: 0,false, listOf())
            data = DataContainer(dayContainers)
            recyclerView?.adapter = TodoRecyclerViewAdapter(data?.data?.get(index ?: 0)?.tasks ?: listOf(),this)
            saveData()
            dialog.cancel()
        }
        dialog.show()
    }

    private fun saveData() {
        val fileOutputStream = openFileOutput("preferences.json", Context.MODE_PRIVATE)
        val outputStreamWriter = OutputStreamWriter(fileOutputStream)
        val mapper = jacksonObjectMapper()
        mapper.writeValue(outputStreamWriter,data)
        outputStreamWriter.close()
        fileOutputStream.close()
        checkCompletedDay()
    }

    private fun checkCompletedDay() {
        var flag = true
        val tasks = data?.data?.get(index ?: 0)?.tasks
        for(task in tasks ?: listOf()){
            flag = flag && task.completed
        }
        if(tasks.isNullOrEmpty()){
            flag = false
        }
        if(flag) {
            completeButton?.visibility = View.VISIBLE
        } else {
            completeButton?.visibility = View.GONE
        }

    }

    private fun completeDay() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
            .setCancelable(true)
            .setMessage("Would you like to mark today task as complete")
            .setPositiveButton("Yes") { _, _ ->
                val tasks: List<Task>? = data?.data?.get(index ?: 0)?.tasks
                val dayContainer = tasks?.let { it1 -> index?.let { it2 -> DayContainer(it2, true, it1) } }
                val dayContainers: MutableList<DayContainer> = data?.data?.toMutableList() ?: mutableListOf()
                dayContainers[index ?: 0] = dayContainer ?: DayContainer(index ?: 0,false, listOf())
                data = DataContainer(dayContainers)
                recyclerView?.adapter = TodoRecyclerViewAdapter(data?.data?.get(index ?: 0)?.tasks ?: listOf(),this)
                saveData()
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    fun completeTask(taskIndex: Int) {
        var tasks: List<Task>? = data?.data?.get(index ?: 0)?.tasks
        val mutableTasks: MutableList<Task>? = tasks?.toMutableList()
        mutableTasks?.set(taskIndex, tasks?.get(taskIndex)?.let { Task(it.name, true) } ?: Task("",false))
        tasks = mutableTasks?.toList()
        val dayContainer = tasks?.let { it1 -> index?.let { it2 -> DayContainer(it2, false, it1) } }
        val dayContainers: MutableList<DayContainer> = data?.data?.toMutableList() ?: mutableListOf()
        dayContainers[index ?: 0] = dayContainer ?: DayContainer(index ?: 0,false, listOf())
        data = DataContainer(dayContainers)
        recyclerView?.adapter = TodoRecyclerViewAdapter(data?.data?.get(index ?: 0)?.tasks ?: listOf(),this)
        saveData()
    }
}