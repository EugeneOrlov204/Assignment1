package com.shpp.eorlov.assignment1.utils.ext

import android.graphics.drawable.Drawable
import android.graphics.Bitmap

import android.R.drawable
import android.graphics.Canvas

import android.graphics.drawable.BitmapDrawable




fun Drawable.drawableToBitmap(): Bitmap? {
    if (this is BitmapDrawable) {
        return this.bitmap
    }

    val bitmap = Bitmap.createBitmap(
        this.intrinsicWidth,
        this.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    this.setBounds(0, 0, canvas.width, canvas.height)
    this.draw(canvas)

    return bitmap
}
