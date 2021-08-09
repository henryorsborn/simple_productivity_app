package com.example.c232finalproject.consistencyiskey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

class TodoActivity : AppCompatActivity() {

    var index: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)
        val recyclerViewAdapter: RecyclerView = findViewById(R.id.todo_recycler)
        findViewById<FloatingActionButton>(R.id.addButton).setOnClickListener {
            addTask()
            recyclerViewAdapter.adapter = TodoRecyclerViewAdapter(loadData(),this)
        }
        index = intent.extras?.getInt("key")
        recyclerViewAdapter.layoutManager = LinearLayoutManager(this)
        recyclerViewAdapter.adapter = TodoRecyclerViewAdapter(loadData(),this)
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
        val container: DataContainer = mapper.readValue(stringBuilder.toString(),DataContainer::class.java)
        return container.data[index ?: 0].tasks
    }

    private fun addTask() {
        //todo implement
    }

}