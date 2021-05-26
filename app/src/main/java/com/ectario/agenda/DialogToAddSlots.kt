package com.ectario.agenda

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment


class DialogToAddSlots : AppCompatDialogFragment() {
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
                    val startTime = editTextStartTime!!.text.toString()
                    val endTime = editTextEndTime!!.text.toString()
                    listener!!.applyAdd(
                        HourSlot(
                            startTime.toFloat(),
                            endTime.toFloat(),
                            "NameTest"
                        )
                    )
                } catch (e : NumberFormatException) {
                    Toast.makeText(context, "Activité non sauvegardée car pas remplie", Toast.LENGTH_SHORT).show()
                }
            }
        editTextStartTime = view.findViewById(R.id.edit_start_time_add)
        editTextEndTime = view.findViewById(R.id.edit_end_time_add)
        return builder?.create()!!
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

    interface AddSlotsDialogListener {
        fun applyAdd(hs: HourSlot)
    }
}