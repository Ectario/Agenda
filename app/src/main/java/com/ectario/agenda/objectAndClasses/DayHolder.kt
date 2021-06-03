package com.ectario.agenda.objectAndClasses

object DayHolder {
    lateinit var currentDay: Day

    enum class NameOfDays(val dayName: String) {
        MONDAY("Lundi"),
        TUESDAY("Mardi"),
        WEDNESDAY("Mercredi"),
        THURSDAY("Jeudi"),
        FRIDAY("Vendredi"),
        SATURDAY("Samedi"),
        SUNDAY("Dimanche"),
        TO_DO("To do")
    }

}
