package com.ectario.agenda

/**
 *  Times need to be given in HOUR not in DECIMAL. (like 4.45 to mean 4H45).
 *  Format time is 24H.
 *
 *  To translate hour to decimal : hour+(min/0.6)
 *  ex : 3H50 -> 3 + ((0.50*100) /60) ~ 3.83
 *
 *  The Reciprocal : hour+(min*0.6)
 *  ex : 3.83 -> 3H ((0.83*100)*0.6) ~ 3H50
 */

class HourSlot(val startTime: Float, val endTime: Float, val slotName: String) {
    val duration = endTime - startTime

    companion object {

        /**
         * Translate the decimal in hour.
         *
         */

        fun translateDecimalToHour(dec: Float): Float {
            return (dec.round(0) + ((dec - dec.round(0)) * 0.6)).toFloat().round(2)
        }

        /**
         * Translate the hour in decimal.
         *
         * @param WARNING The param need to be a float and his decimals need to be included in .0 to .59 (like minutes for an hour)
         */
        @Suppress("KDocUnresolvedReference")
        fun translateHourToDecimal(hour: Float): Float {
            return ((hour.round(0) + ((hour - hour.round(0)) / 0.6)).toFloat())
        }

        /**
         *  Format the string Hour like ..H..
         *  example : 15 -> 15h00
         *      and 15.1 -> 15h10
         *      and 3 -> 3H00
         *  @param hour this is the hour in HOUR and not in DECIMAL
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