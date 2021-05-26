package com.ectario.agenda

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DayActivity : DialogToAddSlots.AddSlotsDialogListener, AppCompatActivity() {
    private lateinit var day : Day
    private lateinit var btn_add : FloatingActionButton

    companion object {
        private var is_btn_add_clicked = false //to unable the spam click
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day)
        btn_add = findViewById(R.id.addSlot)
        init()
    }

    private fun init(){
        day = DayHolder.currentDay

        btn_add.setOnClickListener {
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

    @SuppressLint("SetTextI18n")
    private fun refreshDisplay(){
        this.findViewById<LinearLayout>(R.id.linearLayoutDay).removeAllViews()
        day.timeSlots.sortBy { it.startTime }
        day.timeSlots.forEach {
            var tv = TextView(applicationContext)
            tv.text = HourSlot.formattingHour(it.startTime) + " to " + HourSlot.formattingHour(it.endTime) + " : " + it.slotName
            tv.textSize = 20f
            this.findViewById<LinearLayout>(R.id.linearLayoutDay)?.addView(tv)
        }
    }

    private fun openDialog() {
        val dialog = DialogToAddSlots()
        dialog.show(supportFragmentManager, "example dialog")
    }

    override fun applyAdd(hs: HourSlot) {
        //We need to apply the new slot
        day.addSlot(hs, forcing = true)
        refreshDisplay()

    }
}