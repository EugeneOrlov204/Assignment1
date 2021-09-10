package com.shpp.eorlov.assignment1.utils

import androidx.recyclerview.widget.DiffUtil
import com.shpp.eorlov.assignment1.models.UserModel

class UserItemDiffCallback : DiffUtil.ItemCallback<UserModel>() {
    override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean =
        oldItem == newItem
}