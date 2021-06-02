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
    private lateinit var mViewModelTimePicker: AddObserverTimePicker
    private var mBtnTimePickerStartTime: ImageButton? = null
    private var mBtnTimePickerEndTime: ImageButton? = null
    private var mIsBtnTimePickerClicked = false
    private var mEditTextStartTime: EditText? = null
    private var mEditTextEndTime: EditText? = null
    private var mEditTextActivityName: EditText? = null
    private var mListener: AddSlotsDialogListener? = null

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
                    var changeNeeded = false
                    var startTime = mEditTextStartTime!!.text.toString()
                    var endTime = mEditTextEndTime!!.text.toString()
                    val activityName = mEditTextActivityName!!.text.toString()

                    startTime = startTime.replace(Regex("""[,:hH]"""), ".")
                    endTime = endTime.replace(Regex("""[,:hH]"""), ".")

                    var startTimeFloat = (startTime.toFloat() % 24)
                    var endTimeFloat = (endTime.toFloat() % 24)

                    if (startTimeFloat != startTime.toFloat() || endTimeFloat != endTime.toFloat()) changeNeeded = true

                    startTimeFloat = if(startTimeFloat.decimalPart() > 0.59) {changeNeeded = true; startTimeFloat-startTimeFloat.decimalPart() } else startTimeFloat
                    endTimeFloat = if(endTimeFloat.decimalPart() > 0.59) {changeNeeded = true; endTimeFloat-endTimeFloat.decimalPart() } else endTimeFloat

                    if(activityName == ""){
                        Toast.makeText(
                            context,
                            "Nom d'activité vide [Attention].",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    if(changeNeeded == true){
                        Toast.makeText(
                            context,
                            "Heure hors format 24H/60min [Attention].",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    if(endTimeFloat <= startTimeFloat) {
                        Toast.makeText(
                            context,
                            "Heure de début et de fin non cohérentes. [Erreur]",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        mListener!!.applyAdd(
                            HourSlot(
                                startTimeFloat,
                                endTimeFloat,
                                activityName
                            )
                        )
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(
                        context,
                        "Activité non sauvegardée car elle a été mal remplie. [Erreur]",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        mEditTextStartTime = view.findViewById(R.id.edit_start_time_add)
        mEditTextEndTime = view.findViewById(R.id.edit_end_time_add)
        mBtnTimePickerStartTime = view.findViewById(R.id.btn_timepicker_add_starttime)
        mBtnTimePickerEndTime = view.findViewById(R.id.btn_timepicker_add_endtime)
        mEditTextActivityName = view.findViewById(R.id.edit_activity_name_add)

        mBtnTimePickerStartTime?.setOnClickListener {
            val scale = 1.3f
            if (!mIsBtnTimePickerClicked) {
                mIsBtnTimePickerClicked = true
                it.animate().scaleXBy(scale).scaleYBy(scale).withEndAction {
                    it.animate().scaleXBy(-scale - 1).scaleYBy(-scale - 1).withEndAction {
                        it.animate().scaleX(1f).scaleY(1f).withEndAction {
                            showTimePickerDialog(TimePickerFragment.CHANGE_STARTTIME)
                            mIsBtnTimePickerClicked = false
                        }
                    }
                }.setDuration(200).start()
            }
        }

        mBtnTimePickerEndTime?.setOnClickListener {
            val scale = 1.3f
            if (!mIsBtnTimePickerClicked) {
                mIsBtnTimePickerClicked = true
                it.animate().scaleXBy(scale).scaleYBy(scale).withEndAction {
                    it.animate().scaleXBy(-scale - 1).scaleYBy(-scale - 1).withEndAction {
                        it.animate().scaleX(1f).scaleY(1f).withEndAction {
                            showTimePickerDialog(TimePickerFragment.CHANGE_ENDTIME)
                            mIsBtnTimePickerClicked = false
                        }
                    }
                }.setDuration(200).start()
            }
        }

        //Assign the viewModel to share data between the dialog for adding and the timepicker
        mViewModelTimePicker = activity?.run {
            ViewModelProvider(this).get(AddObserverTimePicker::class.java)
        } ?: throw Exception("Invalid Activity")

        //Observe the result of the time picker
        mViewModelTimePicker.endTime.observe(this, {
            mEditTextEndTime?.setText(mViewModelTimePicker.endTime.value)
        })

        mViewModelTimePicker.startTime.observe(this, {
            mEditTextStartTime?.setText(mViewModelTimePicker.startTime.value)
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
        mListener = try {
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