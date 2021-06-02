package com.ectario.agenda

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


class Adapter(ctx: Context?, var dayList: List<Day>) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {
    private var inflater: LayoutInflater = LayoutInflater.from(ctx)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflater.inflate(R.layout.custom_main_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTextview.text = dayList[position].name
        animate(holder.itemView, position)

        if(dayList[position].name == DayHolder.NameOfDays.TO_DO.dayName){
            holder.itemTextview.setTextColor(ContextCompat.getColor(holder.itemTextview.context, R.color.purple_200))
        }
    }


    override fun getItemCount(): Int {
        return dayList.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemTextview: TextView = itemView.findViewById(R.id.textView)

        init {
            itemView.setOnClickListener {
                it.animate().rotationX(it.rotationX + 360f).setDuration(350).withEndAction {
                    val intent = Intent(it.context, DayActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) //Allow to start an Activity from here
                    DayHolder.currentDay = dayList[layoutPosition]
                    it.context.startActivity(intent)
                }
            }
        }
    }


    private fun animate(view: View, pos: Int) {
        view.animate().cancel()
        view.translationY = 100f
        view.alpha = 0f
        view.animate().alpha(1.0f).translationY(0f).setDuration(300).startDelay =
            (pos * 100).toLong()
    }

}