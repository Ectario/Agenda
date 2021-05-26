package com.ectario.agenda

object DayHolder {
    lateinit var currentDay: Day

    enum class NameOfDays(val dayName: String) {
        LUNDI("Lundi"),
        MARDI("Mardi"),
        MERCREDI("Mercredi"),
        JEUDI("Jeudi"),
        VENDREDI("Vendredi"),
        SAMEDI("Samedi"),
        DIMANCHE("Dimanche"),
        TO_DO("To do")
    }
}
