package com.ectario.agenda

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.ViewModelProvider


class DialogToAddSlots : AppCompatDialogFragment() {
    private lateinit var viewModelTimePicker: AddObserverTimePicker
    private var btnTimePickerStartTime: ImageButton? = null
    private var btnTimePickerEndTime: ImageButton? = null
    private var isBtnTimePickerClicked = false
    private var editTextStartTime: EditText? = null
    private var editTextEndTime: EditText? = null
    private var listener: AddSlotsDialogListener? = null

    @SuppressLint("ShowToast")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder? = activity?.let { AlertDialog.Builder(it) }
        val inflater = activity!!.layoutInflater
        val view: View = inflater.inflate(R.layout.fragment_dialog_add, null)
        builder?.setView(view)
            ?.setTitle("Ajout d'une activité")
            ?.setNegativeButton(
                "Annuler"
            ) { _, _ -> }
            ?.setPositiveButton(
                "Ok"
            ) { _, _ ->
                try {
                    var startTime = editTextStartTime!!.text.toString()
                    var endTime = editTextEndTime!!.text.toString()

                    startTime = startTime.replace(Regex("""[,:hH]"""), ".")
                    endTime = endTime.replace(Regex("""[,:hH]"""), ".")

                    listener!!.applyAdd(
                        HourSlot(
                            startTime.toFloat(),
                            endTime.toFloat(),
                            "NameTest"
                        )
                    )
                } catch (e: NumberFormatException) {
                    Toast.makeText(
                        context,
                        "Activité non sauvegardée car elle a été mal remplie.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        editTextStartTime = view.findViewById(R.id.edit_start_time_add)
        editTextEndTime = view.findViewById(R.id.edit_end_time_add)
        btnTimePickerStartTime = view.findViewById(R.id.btn_timepicker_add_starttime)
        btnTimePickerEndTime = view.findViewById(R.id.btn_timepicker_add_endtime)

        btnTimePickerStartTime?.setOnClickListener {
            val scale = 1.3f
            if (!isBtnTimePickerClicked) {
                isBtnTimePickerClicked = true
                it.animate().scaleXBy(scale).scaleYBy(scale).withEndAction {
                    it.animate().scaleXBy(-scale - 1).scaleYBy(-scale - 1).withEndAction {
                        it.animate().scaleX(1f).scaleY(1f).withEndAction {
                            showTimePickerDialog(TimePickerFragment.CHANGE_STARTTIME)
                            isBtnTimePickerClicked = false
                        }
                    }
                }.setDuration(200).start()
            }
        }

        btnTimePickerEndTime?.setOnClickListener {
            val scale = 1.3f
            if (!isBtnTimePickerClicked) {
                isBtnTimePickerClicked = true
                it.animate().scaleXBy(scale).scaleYBy(scale).withEndAction {
                    it.animate().scaleXBy(-scale - 1).scaleYBy(-scale - 1).withEndAction {
                        it.animate().scaleX(1f).scaleY(1f).withEndAction {
                            showTimePickerDialog(TimePickerFragment.CHANGE_ENDTIME)
                            isBtnTimePickerClicked = false
                        }
                    }
                }.setDuration(200).start()
            }
        }

        //Assign the viewModel to share data between the dialog for adding and the timepicker
        viewModelTimePicker = activity?.run {
            ViewModelProvider(this).get(AddObserverTimePicker::class.java)
        } ?: throw Exception("Invalid Activity")

        //Observe the result of the time picker
        viewModelTimePicker.endTime.observe(this, {
            editTextEndTime?.setText(viewModelTimePicker.endTime.value)
        })

        viewModelTimePicker.startTime.observe(this, {
            editTextStartTime?.setText(viewModelTimePicker.startTime.value)
        })

        //Return an AlertDialog initialized
        return builder?.create()!!
    }

    //Display the timePicker
    private fun showTimePickerDialog(actionTag: String) {
        TimePickerFragment(actionTag).show(activity!!.supportFragmentManager, "timePickerAdd")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try {
            context as AddSlotsDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                context.toString() +
                        "must implement AddSlotsDialogListener"
            )
        }
    }

    //Allow the overriding to use the entry
    interface AddSlotsDialogListener {
        fun applyAdd(hs: HourSlot)
    }
}