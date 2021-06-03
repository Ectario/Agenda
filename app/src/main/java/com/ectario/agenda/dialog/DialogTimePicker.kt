package com.ectario.agenda.dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.ectario.agenda.AddObserverTimePicker

class DialogTimePicker(private val actionTag : String) : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private lateinit var mViewModelTimePicker : AddObserverTimePicker

    companion object {
        //actionTag available
        const val CHANGE_ENDTIME = "endTimeChange"
        const val CHANGE_STARTTIME = "startTimeChange"
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: android.widget.TimePicker?, hourOfDay: Int, minute: Int) {
        // Do something with the time chosen by the user
        // Modify the view model which is used in DialogToAddSlots

        mViewModelTimePicker = activity?.run {
            ViewModelProvider(this).get(AddObserverTimePicker::class.java)
        } ?: throw Exception("Invalid Activity")

        if(actionTag == CHANGE_ENDTIME) {
            if(minute<10) mViewModelTimePicker.endTime.value = "${hourOfDay}H0${minute}"
            else mViewModelTimePicker.endTime.value = "${hourOfDay}H${minute}"
        }

        else if(actionTag == CHANGE_STARTTIME) {
            if(minute<10) mViewModelTimePicker.startTime.value = "${hourOfDay}H0${minute}"
            else mViewModelTimePicker.startTime.value = "${hourOfDay}H${minute}"
        }
    }
}
