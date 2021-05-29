package com.ectario.agenda

import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ectario.agenda.tools.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DayActivity : DialogToAddSlots.AddSlotsDialogListener, AppCompatActivity() {
    private lateinit var day : Day
    private lateinit var btnAdd : FloatingActionButton

    companion object {
        private var is_btn_add_clicked = false //to unable the spam click
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day)
        btnAdd = findViewById(R.id.addSlot)
        init()
    }

    private fun init(){
        day = DayHolder.currentDay

        btnAdd.setOnClickListener {
            val scale = 1.4f
            if (!is_btn_add_clicked){
                is_btn_add_clicked = true
                it.animate().scaleXBy(scale).scaleYBy(scale).withEndAction {
                    it.animate().scaleXBy(-scale - 1).scaleYBy(-scale - 1).withEndAction {
                        it.animate().scaleX(1.1f).scaleY(1.1f).withEndAction {
                            //Add an HourSlot
                            openDialog()
                            is_btn_add_clicked = false
                        }
                    }
                }.setDuration(200).start()
            }
        }

        refreshDisplay()
    }

    private fun refreshDisplay(){
        val activityColumnView = this.findViewById<LinearLayout>(R.id.activity_line_id)
        val hourColumnView = this.findViewById<LinearLayout>(R.id.hour_column_id)

        val listHour = ArrayList<Float>()

        //Erase the past content
        activityColumnView.removeAllViews()
        hourColumnView.removeAllViews()

        day.timeSlots.sortBy { it.startTime }
        day.timeSlots.forEach {
            listHour.add(it.startTime)
            listHour.add(it.endTime)
            val tvActivity = TextView(applicationContext)

            tvActivity.text = it.slotName
            tvActivity.textSize = 20f
            tvActivity.gravity = Gravity.CENTER_HORIZONTAL

            tvActivity.setTextColor(getColor(R.color.black))
            activityColumnView.addView(tvActivity)
        }

        listHour.sort()

        listHour.forEach{
            val tvHour = TextView(applicationContext)
            tvHour.setMargins(top = 16)
            tvHour.setTextColor(getColor(R.color.black))
            tvHour.text = HourSlot.formattingHour(it)
            hourColumnView.addView(tvHour)
        }

    }

    private fun openDialog() {
        val dialog = DialogToAddSlots()
        dialog.show(supportFragmentManager, "dialog")
    }


    override fun applyAdd(hs: HourSlot) {
        //We need to apply the new slot
        day.addSlot(hs, forcing = true)
        refreshDisplay()

    }
}