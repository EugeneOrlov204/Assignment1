package com.shpp.eorlov.assignment1.ui.imageLoaderDialog.adapter.viewHolder

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment1.databinding.PhotoListItemBinding

class UserPhotosViewHolder(
    private val binding: PhotoListItemBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindTo(imagePath: String) {
        binding.imageViewUserImage.setImageURI(imagePath.toUri())
    }
}