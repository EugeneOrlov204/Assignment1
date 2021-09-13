package com.shpp.eorlov.assignment1.utils

import androidx.recyclerview.widget.DiffUtil
import com.shpp.eorlov.assignment1.models.UserModel

class UserPhotoItemDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem
}