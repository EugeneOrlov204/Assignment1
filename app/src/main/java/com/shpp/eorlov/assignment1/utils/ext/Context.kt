package com.shpp.eorlov.assignment1.utils.ext

import android.content.Context

fun Context.dpToPx(dp: Int): Float {
    return dp.toFloat() * this.resources.displayMetrics.density
}