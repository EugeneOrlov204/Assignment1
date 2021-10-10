package com.shpp.eorlov.assignment1.ui.imageLoaderDialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.shpp.eorlov.assignment1.databinding.PhotoListItemBinding
import com.shpp.eorlov.assignment1.ui.imageLoaderDialog.adapter.viewHolder.UserPhotosViewHolder
import com.shpp.eorlov.assignment1.utils.Constants

class ImageLoaderRecyclerAdapter() : ListAdapter<String, UserPhotosViewHolder>(
    UserPhotoItemDiffCallback()
) {
    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPhotosViewHolder {
        return UserPhotosViewHolder(
            PhotoListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: UserPhotosViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun getItemId(position: Int): Long = position.toLong()
}