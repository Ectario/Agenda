package com.ectario.agenda

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.ectario.agenda.dialog.DialogToAddSlots
import com.ectario.agenda.objectsAndClasses.Day
import com.ectario.agenda.objectsAndClasses.DayManager
import com.ectario.agenda.objectsAndClasses.HourSlot
import com.ectario.agenda.objectsAndClasses.SaveManager
import com.ectario.agenda.tools.dpToPx
import com.ectario.agenda.tools.setMargins
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class DayActivity : DialogToAddSlots.AddSlotsDialogListener, AppCompatActivity() {
    private lateinit var day: Day
    private lateinit var btnAdd: FloatingActionButton


    companion object {
        private var is_btn_add_clicked = false //to unable the spam click
        private const val ACTIVITY_COLUMN_VIEW_SEPARATOR_HEIGHT = 2f //in dp
        private const val BLANK_TEXTVIEW_TEXT = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day)
        btnAdd = findViewById(R.id.add_btn_id)
        init()
    }

    private fun init() {
        day = DayManager.currentDay

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
            HashMap<String, Triple<Int, Int, Int>>() // key = formatted hour in the column, value = ( start, middle , end) -> To format the activity column with the y axe

        //Erase the past content
        activityColumnView.removeAllViews()
        hourColumnView.removeAllViews()

        //Write the content

        //Sort the hour
        sortDayTimesSlots(listHour)
        //Display hours
        displayHours(listHour, listHourView, hourColumnView, yHour)

        //Display activity
        displayActivity(activityColumnView, yHour)

    }

    private fun displayActivity(
        activityColumnView: LinearLayout,
        yHour: HashMap<String, Triple<Int, Int, Int>>
    ) {
        day.timeSlots.forEachIndexed { i, it ->

            //Display the separator [start]
            addAndDisplaySeparator(activityColumnView)

            //display the activity name
            displayActivityName(it, activityColumnView, yHour)

            //Display the separator [end]
            addAndDisplaySeparator(activityColumnView)

            //Add blank space between activities
            addAndDisplayBlankspace(i, activityColumnView, yHour)

        }
    }

    private fun addAndDisplayBlankspace(
        index: Int,
        activityColumnView: LinearLayout,
        yHour: HashMap<String, Triple<Int, Int, Int>>
    ) {
        val next: HourSlot? = if (day.timeSlots.size > index + 1) day.timeSlots[index + 1] else null
        if (next != null) {
            val tvBlankActivity = TextView(applicationContext)
            tvBlankActivity.text = BLANK_TEXTVIEW_TEXT
            tvBlankActivity.setTextColor(getColor(R.color.black))
            activityColumnView.addView(tvBlankActivity)

            //Reconfigure the height of the textview to allow the centering and the alignment for the next activity
            @Suppress("NAME_SHADOWING") val wrapSpec: Int =
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            (tvBlankActivity.parent as View).measure(
                wrapSpec,
                wrapSpec
            )

            val lastBlankChild = activityColumnView.children.last()

            if (lastBlankChild is TextView) {
                @Suppress("NAME_SHADOWING") val wrapSpec: Int =
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                (lastBlankChild.parent as View).measure(wrapSpec, wrapSpec)
                val offset = (lastBlankChild.parent as View).measuredHeight
                lastBlankChild.height = (
                        yHour[HourSlot.formattingHour(next.startTime)]!!.second - offset
                        )
            } else error("Child in activityColumnView must all be a textview")
        }
    }

    private fun displayActivityName(
        hourSlot: HourSlot,
        activityColumnView: LinearLayout,
        yHour: HashMap<String, Triple<Int, Int, Int>>
    ) {
        val tvActivity = TextView(applicationContext)
        tvActivity.text = hourSlot.slotName
        tvActivity.textSize = 20f
        tvActivity.gravity = Gravity.CENTER
        tvActivity.setTextColor(getColor(R.color.black))
        tvActivity.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.light_gray
            )
        ) //add click listener [to delete]
        tvActivity.setOnClickListener { _ ->
            Snackbar.make(
                this.findViewById(R.id.constraint_container),
                "Supprimer le créneau ${hourSlot.slotName} ?", //"it" is not the local variable of the setOnClickListener (which is shadowed by the _)
                Snackbar.LENGTH_LONG
            ).setAction("Oui", DeleteListener(tvActivity, hourSlot)).show()
        }
        activityColumnView.addView(tvActivity)

        //Reconfigure the height of the textview to allow the centering and the alignment for the separator
        val wrapSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        (tvActivity.parent as View).measure(wrapSpec, wrapSpec)

        val lastChild = activityColumnView.children.last()

        if (lastChild is TextView) {
            @Suppress("NAME_SHADOWING") val wrapSpec =
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            (lastChild.parent as View).measure(
                wrapSpec,
                wrapSpec
            )
            val offset = (lastChild.parent as View).measuredHeight
            lastChild.height = (
                    yHour[HourSlot.formattingHour(hourSlot.endTime)]!!.third - offset
                    )
        } else error("Child in activityColumnView must all be a textview")
    }

    private fun addAndDisplaySeparator(activityColumnView: LinearLayout) {
        val activitySeparator = TextView(applicationContext)
        activitySeparator.height = dpToPx(ACTIVITY_COLUMN_VIEW_SEPARATOR_HEIGHT)
        activitySeparator.width = activityColumnView.width
        activitySeparator.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.dark_gray_high
            )
        )
        activityColumnView.addView(activitySeparator)
    }

    private fun displayHours(
        listHour: ArrayList<Float>,
        listHourView: ArrayList<TextView>,
        hourColumnView: LinearLayout,
        yHour: HashMap<String, Triple<Int, Int, Int>>
    ) {
        listHour.forEachIndexed { i, it ->
            //Display each hour
            val tvHour = TextView(applicationContext)
            tvHour.setMargins(top = 16)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tvHour.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)
            }
            tvHour.setTextColor(getColor(R.color.black))
            tvHour.text = HourSlot.formattingHour(it)
            tvHour.setTypeface(null, Typeface.BOLD)
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
            } else error("Child in hourColumnView must all be a textview")

            //display each exact hour
            val next: Int = if (listHour.size > i + 1) listHour[i + 1].round(0).toInt() else -1
            if (next > -1) fillAndDisplayHour(
                it.round(0).toInt() + 1,
                if (next.toFloat() == listHour[i + 1]) next else next + 1,
                hourColumnView
            )
        }
    }

    private fun sortDayTimesSlots(listHour: ArrayList<Float>) {
        day.timeSlots.sortBy { it.startTime }

        day.timeSlots.forEach {
            listHour.add(it.startTime)
            listHour.add(it.endTime)
        }

        listHour.sort()
    }


    /**
     * both startTime and endTime are an exact Hour (ex : 4h00 -> 4 is exact. 4H15 is not exact.)
     * startTime is include and endTime is excluded
     */

    private fun fillAndDisplayHour(startTime: Int, endTime: Int, hourViewToFill: LinearLayout) {
        for (i in startTime until endTime) {
            //Display each hour
            val tvHour = TextView(applicationContext)
            tvHour.setMargins(top = 16)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tvHour.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)
            }
            tvHour.setTextColor(getColor(R.color.black))
            tvHour.text = HourSlot.formattingHour(i.toFloat())
            hourViewToFill.addView(tvHour)
        }
    }

    private inner class DeleteListener(val viewToApplyAnimation: View, val hourSlot: HourSlot) :
        View.OnClickListener {
        override fun onClick(v: View?) {
            val animation1: Animation =
                AnimationUtils.loadAnimation(applicationContext, R.anim.delete_anim)
            viewToApplyAnimation.startAnimation(animation1)

            val h = Handler(Looper.myLooper()!!)
            h.postDelayed(
                {
                    day.timeSlots.remove(hourSlot)
                    SaveManager.saveWeek(applicationContext)
                    refreshDisplay()
                }, applicationContext.resources.getInteger(R.integer.delete_anim_duration).toLong()
            )
        }
    }

    private fun openDialog() {
        val dialog = DialogToAddSlots()
        dialog.show(supportFragmentManager, "dialog")
    }

    override fun applyAdd(hs: HourSlot) {
        //We need to apply the new slot
        day.addSlot(hs, forcing = true)
        SaveManager.saveWeek(applicationContext)
        refreshDisplay()

    }
}