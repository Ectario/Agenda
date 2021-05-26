package com.ectario.agenda

class Day (val name : String, val timeSlots : ArrayList<HourSlot> = ArrayList()){ // (Time format is 24H)

    /**
     * @param HourSlot to add, his name, and a bool to force the adding (if other HourSlot collides then they're released)
     * @return true if the HourSlot is added, and false if it was colliding with another
     */

    fun addSlot(hs : HourSlot, forcing : Boolean = false) : Boolean {
        val listToRemove = ArrayList<HourSlot>()
        timeSlots.forEach{
            if(!forcing && collidingSlots(hs, it)) return false
            else if (collidingSlots(hs, it)) listToRemove.add(it)
        }
        timeSlots.removeAll(listToRemove)
        timeSlots.add(hs)
        return true
    }


    private fun collidingSlots(hs1 : HourSlot, hs2 : HourSlot) : Boolean {
        return (hs1.startTime <= hs2.startTime && hs1.endTime >= hs2.startTime) || (hs2.startTime <= hs1.startTime && hs2.endTime >= hs1.startTime)
    }


}



