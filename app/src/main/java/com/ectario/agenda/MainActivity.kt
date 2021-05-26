package com.ectario.agenda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

        ///TEST

        var hmap = ArrayList<HourSlot>()

        hmap.add(HourSlot(11.7f, 13f, "Heure du test 2"))
        var hs1 = HourSlot(8.7f, 10f, "Heure du test")
        hmap.add(hs1)
        hmap.add(HourSlot(13.7f, 16f, "Heure du test 3"))
        dayList.add(Day("Test", hmap))

        ///

        adapter = Adapter(applicationContext, dayList)

        val gridLayoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL,false)
        dataListRecycler.layoutManager = gridLayoutManager
        dataListRecycler.adapter = adapter
    }
}
