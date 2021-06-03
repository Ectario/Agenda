package com.ectario.agenda.objectsAndClasses

import com.ectario.agenda.round


/**
 *  Times need to be given in HOUR not in DECIMAL. (like 4.45 to mean 4H45).
 *  Format time is 24H.
 *
 *  To translate hour to decimal : hour+(min/0.6)
 *  ex : 3H50 -> 3 + ((0.50*100) /60) ~ 3.83
 *
 *  The Reciprocal : hour+(min*0.6)
 *  ex : 3.83 -> 3H ((0.83*100)*60/100) ~ 3H50
 */

class HourSlot(val startTime: Float, val endTime: Float, val slotName: String) {

    companion object {

        fun translateDecimalToHour(dec: Float): Float {
            return (dec.round(0) + ((dec - dec.round(0)) * 0.6)).toFloat().round(2)
        }

        /**
         * @param hour WARNING The param need to be a float between .01 and 0.59 included.
         */
        fun translateHourToDecimal(hour: Float): Float {
            if((hour - hour.round(0) >= 0.6)) {
                error("The decimal part in translateHourToDecimal must be between .01 and 0.59 : ${(hour - hour.round(0))}")
            }
            return ((hour.round(0) + ((hour - hour.round(0)) / 0.6)).toFloat())
        }

        /**
         *  Format the string Hour like ..H..
         *  example : 15 -> 15h00
         *      and 15.1 -> 15h10
         *      and 3 -> 3H00
         *  @param hour this is the hour in HOUR and not in DECIMAL : like 3.3 to mean 3h30 and NOT 3.5 to mean 3h30
         */

        fun formattingHour(hour: Float) : String {
            var strH: String = hour.toString()
            strH = strH.replace('.','H')
            val arrH = (strH.split('H')).toTypedArray()
            val ctH = if (arrH.size > 1) arrH[1].length else 0
            if (ctH < 2) {
                strH+="0"
            }
            return strH
        }
    }
}