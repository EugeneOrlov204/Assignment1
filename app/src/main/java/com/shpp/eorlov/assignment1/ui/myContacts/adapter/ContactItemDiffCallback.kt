package com.shpp.eorlov.assignment1.ui.myContacts.adapter

import androidx.recyclerview.widget.DiffUtil
import com.shpp.eorlov.assignment1.model.UserModel

class ContactItemDiffCallback : DiffUtil.ItemCallback<UserModel>() {
    override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean =
        oldItem == newItem
}