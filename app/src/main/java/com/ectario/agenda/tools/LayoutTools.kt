package com.ectario.agenda.tools

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop


fun View.setMargins(
    left: Int = this.marginLeft,
    top: Int = this.marginTop,
    right: Int = this.marginRight,
    bottom: Int = this.marginBottom
) {
    var param = this.layoutParams as ViewGroup.MarginLayoutParams?
    if (param == null) {
        param =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
    }

    param.setMargins(left, top, right, bottom)
    this.layoutParams = param
}

fun Context.dpToPx(dp: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
fun Context.spToPx(sp: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics).toInt()

fun View.setHeight(height : Int, match_parent : Boolean = false){
    var param = this.layoutParams as ViewGroup.MarginLayoutParams?
    if (param == null) {
        param =
            LinearLayout.LayoutParams(
                WRAP_CONTENT,
                if (match_parent) ViewGroup.LayoutParams.MATCH_PARENT
                else WRAP_CONTENT
            )
    }

    if (!match_parent) param.height = height
    this.layoutParams = param
}

fun View.setWidth(width : Int, match_parent: Boolean = false){
    var param = this.layoutParams as ViewGroup.MarginLayoutParams?
    if (param == null) {
        param =
            LinearLayout.LayoutParams(
                if (match_parent) ViewGroup.LayoutParams.MATCH_PARENT
                else WRAP_CONTENT,
                WRAP_CONTENT
            )
    }

    if (!match_parent) param.width = width
    this.layoutParams = param
}


fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

fun getScreenHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}