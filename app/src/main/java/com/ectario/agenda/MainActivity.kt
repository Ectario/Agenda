package com.ectario.agenda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ectario.agenda.objectAndClasses.Day
import com.ectario.agenda.objectAndClasses.DayHolder

class MainActivity : AppCompatActivity() {
    private var dayList = ArrayList<Day>()
    private lateinit var adapter : Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataListRecycler = findViewById<RecyclerView>(R.id.dataList)

        DayHolder.NameOfDays.values().forEach {
            dayList.add(Day(it.dayName))
        }

        adapter = Adapter(applicationContext, dayList)

        val gridLayoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL,false)
        dataListRecycler.layoutManager = gridLayoutManager
        dataListRecycler.adapter = adapter
    }
}
