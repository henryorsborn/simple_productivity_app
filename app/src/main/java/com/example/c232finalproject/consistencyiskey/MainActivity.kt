package com.example.c232finalproject.consistencyiskey

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.lang.Exception

import java.io.*
import java.lang.RuntimeException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val days: MutableList<Int> = mutableListOf()
        checkNewUser(this)
        findViewById<RecyclerView>(R.id.main_recycler).adapter = MainRecyclerViewAdapter(loadData(),this)
    }

    fun checkNewUser(context: Context){
        var fileInputStream: FileInputStream? = null
        var fileOutputStream: FileOutputStream? = null
        try {
            fileInputStream = openFileInput("base.json")
        } catch (e: Exception){

            val inputStream: InputStream = context.resources.openRawResource(R.raw.blank)
            val inputStreamReader: InputStreamReader = InputStreamReader(inputStream)
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
            var line: String
            val text = java.lang.StringBuilder()
            while (true) {
                try {
                    line = bufferedReader.readLine()
                } catch (e1: RuntimeException){
                    break
                }
                text.append(line);
                text.append('\n');
            }
            try {
                fileOutputStream = application.openFileOutput("base.json", Context.MODE_PRIVATE)
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

    fun loadData(): List<DayContainer>{
        var fileInputStream: FileInputStream? = null
        fileInputStream = openFileInput("base.json")
        var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
        val stringBuilder: StringBuilder = StringBuilder()
        var text: String? = null
        while ({ text = bufferedReader.readLine(); text }() != null) {
            stringBuilder.append(text)
        }
        val mapper = jacksonObjectMapper()
        val container: DataContainer = mapper.readValue(stringBuilder.toString(),DataContainer::class.java)
        return container.data
    }

}