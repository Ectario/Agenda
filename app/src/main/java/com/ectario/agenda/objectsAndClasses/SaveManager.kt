package com.ectario.agenda.objectsAndClasses

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object SaveManager {
    private const val SHARED_PREFERENCES_KEY_WEEK = "week"
    private const val SHARED_PREFERENCES_AGENDA = "shared preferences"
    var week : LinkedHashMap<String, Day>? = null

    fun saveWeek(context: Context) {
        val sharedPreferences: SharedPreferences? = context.getSharedPreferences(
            SHARED_PREFERENCES_AGENDA, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        val gson = Gson()
        val json = gson.toJson(week)
        editor?.putString(SHARED_PREFERENCES_KEY_WEEK, json)
        editor?.apply()
    }

    /**
     * Load the week. Otherwise week is null. Week need to be load just one time during the runtime of Agenda,
     * after what we can just access the static field week.
     */
    fun loadWeek(context: Context) {
        val sharedPreferences: SharedPreferences? = context.getSharedPreferences(
            SHARED_PREFERENCES_AGENDA, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences?.getString(SHARED_PREFERENCES_KEY_WEEK, null)
        val type: Type = object : TypeToken<LinkedHashMap<String, Day>?>() {}.type
        week = if (json!=null) gson.fromJson(json, type) else null
        if (week == null) {
            week  = LinkedHashMap()
            DayManager.NameOfDays.values().forEach {
                week!![it.dayName] = Day(it.dayName)
            }
        }
    }
}