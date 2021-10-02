package com.shpp.eorlov.assignment1.ui.image_loader_dialog_fragment.adapter

import androidx.recyclerview.widget.DiffUtil

class UserPhotoItemDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem
}