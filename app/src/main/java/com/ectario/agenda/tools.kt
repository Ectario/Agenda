package com.ectario.agenda
import kotlin.math.floor


fun Float.round(decimals: Int = 2): Float = if (decimals!=0) "%.${decimals}f".format(this).replace(',','.').toFloat() else floor(this)