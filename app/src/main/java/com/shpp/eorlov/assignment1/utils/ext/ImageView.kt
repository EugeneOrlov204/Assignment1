package com.shpp.eorlov.assignment1.utils.ext

import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide

fun AppCompatImageView.loadImage(url : String) {
    Glide.with(this)
        .load(url)
        .circleCrop()
        .into(this)
}

fun AppCompatImageView.loadImage(resource : Int) {
    Glide.with(this)
        .load(resource)
        .circleCrop()
        .into(this)
}