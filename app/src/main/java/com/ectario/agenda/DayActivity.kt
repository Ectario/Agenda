package com.ectario.agenda

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.ectario.agenda.tools.dpToPx
import com.ectario.agenda.tools.setHeight
import com.ectario.agenda.tools.setMargins
import com.ectario.agenda.tools.setWidth
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DayActivity : DialogToAddSlots.AddSlotsDialogListener, AppCompatActivity() {
    private lateinit var day: Day
    private lateinit var btnAdd: FloatingActionButton


    companion object {
        private var is_btn_add_clicked = false //to unable the spam click
        private const val ACTIVITY_COLUMN_VIEW_SEPARATOR_HEIGHT = 2f //in dp
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day)
        btnAdd = findViewById(R.id.add_btn_id)
        init()
    }

    private fun init() {
        day = DayHolder.currentDay

        btnAdd.setOnClickListener {
            val scale = 1.4f
            if (!is_btn_add_clicked) {
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

    private fun refreshDisplay() {
        val activityColumnView = this.findViewById<LinearLayout>(R.id.activity_line_id)
        val hourColumnView = this.findViewById<LinearLayout>(R.id.hour_column_id)

        val listHour = ArrayList<Float>()
        val listHourView = ArrayList<TextView>()
        val yHour =
            HashMap<String, Triple<Int, Int, Int>>() // key = formatted hour in the column, value = ( start, middle , end) -> To format the activity column

        //Erase the past content
        activityColumnView.removeAllViews()
        hourColumnView.removeAllViews()

        //Write the content

        //Display hours

        //Sort the hour
        day.timeSlots.sortBy { it.startTime }

        day.timeSlots.forEach {
            listHour.add(it.startTime)
            listHour.add(it.endTime)
        }

        listHour.sort()

        var sumOldY = 0

        listHour.forEach {
            //Display each hour
            val tvHour = TextView(applicationContext)
            tvHour.setMargins(top = 16)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                tvHour.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)
            }
            tvHour.setTextColor(getColor(R.color.black))
            tvHour.text = HourSlot.formattingHour(it)
            listHourView.add(tvHour)
            hourColumnView.addView(tvHour)

            //Save the height to allow the alignment of the separator
            val lastChild = hourColumnView.children.last()

            if (lastChild is TextView) {
                val wrapSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                (lastChild.parent as View).measure(wrapSpec, wrapSpec)
                val y = (lastChild.parent as View).measuredHeight
                yHour[lastChild.text.toString()] =
                    Triple(
                        y,
                        y + lastChild.measuredHeight / 2,
                        y + lastChild.measuredHeight
                    )
            } else error("Child in hourColumnView are not all a textview")
        }


        //Display activity
        day.timeSlots.forEach {
            //display the activity name
            val tvActivity = TextView(applicationContext)
            tvActivity.text = it.slotName
            tvActivity.textSize = 20f
            tvActivity.gravity = Gravity.CENTER
            tvActivity.setTextColor(getColor(R.color.black))
            activityColumnView.addView(tvActivity)

            //Reconfigure the height of the textview to allow the centering and the alignment for the separator
            val wrapSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            (tvActivity.parent as View).measure(wrapSpec, wrapSpec)

            val lastChild = activityColumnView.children.last()

            if (lastChild is TextView) {
                val wrapSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                (lastChild.parent as View).measure(wrapSpec, wrapSpec)
                val offset = (lastChild.parent as View).measuredHeight
                lastChild.height = (
                        yHour[HourSlot.formattingHour(it.endTime)]!!.third - offset
                        )
            } else error("Child in activityColumnView are not all a textview")

            //Display the separator
            val activitySeparator = View(applicationContext)
            activitySeparator.setHeight(dpToPx(ACTIVITY_COLUMN_VIEW_SEPARATOR_HEIGHT))
            activitySeparator.setWidth(activityColumnView.width)
            activitySeparator.setBackgroundColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.dark_gray
                )
            )
            activityColumnView.addView(activitySeparator)
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