package com.example.consistencyiskey

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.c232finalproject.consistencyiskey.MainRecyclerViewAdapter
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.lang.Exception
import java.io.*
import java.lang.RuntimeException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkNewUser(this)
        val recyclerViewAdapter: RecyclerView = findViewById(R.id.main_recycler)
        recyclerViewAdapter.layoutManager = LinearLayoutManager(this)
        recyclerViewAdapter.adapter = MainRecyclerViewAdapter(loadData(),this)
    }

    private fun checkNewUser(context: Context){
        var fileInputStream: FileInputStream? = null
        var fileOutputStream: FileOutputStream? = null
        try {
            fileInputStream = openFileInput("preferences.json")
        } catch (e: Exception){

            val inputStream: InputStream = context.resources.openRawResource(R.raw.blank)
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var line: String
            val text = java.lang.StringBuilder()
            while (true) {
                try {
                    line = bufferedReader.readLine()
                } catch (e1: RuntimeException){
                    break
                }
                text.append(line)
                text.append('\n')
            }
            try {
                fileOutputStream = application.openFileOutput("preferences.json", Context.MODE_PRIVATE)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            val outputStreamWriter = OutputStreamWriter(fileOutputStream)
            outputStreamWriter.write(text.toString())
            outputStreamWriter.close()
        } finally {
            fileInputStream?.close()
            fileOutputStream?.close()
        }
    }

    private fun loadData(): List<DayContainer>{
        val fileInputStream: FileInputStream?  = openFileInput("preferences.json")
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
        val container: DataContainer = mapper.readValue(stringBuilder.toString(),DataContainer::class.java)
        return container.data
    }

}