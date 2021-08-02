package com.example.c232finalproject.consistencyiskey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val days: MutableList<Int> = mutableListOf()
        for(i in 0..365) {
            days.add(i)
        }
        val statuses: MutableList<Boolean> = mutableListOf()
        for(i in 0..365) {
            statuses.add(true)
        }
        findViewById<RecyclerView>(R.id.main_recycler).adapter = MainRecyclerViewAdapter(days.toList(), statuses.toList(),this)
    }
}