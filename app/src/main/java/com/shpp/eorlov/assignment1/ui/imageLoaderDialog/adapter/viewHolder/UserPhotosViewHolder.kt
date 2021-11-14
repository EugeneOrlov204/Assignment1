package com.shpp.eorlov.assignment1.ui.imageLoaderDialog.adapter.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment1.databinding.PhotoListItemBinding
import com.shpp.eorlov.assignment1.ui.imageLoaderDialog.adapter.listeners.ImageClickListener
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce
import com.shpp.eorlov.assignment1.utils.ext.loadImage

class UserPhotosViewHolder constructor(
    private val binding: PhotoListItemBinding,
    private val imageClickListener: ImageClickListener
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindTo(imagePath: String) {
        binding.imageViewUserImage.loadImage(imagePath)

        setListeners(imagePath)
    }

    private fun setListeners(imagePath: String) {
        binding.constraintLayoutPhotoListItem.clickWithDebounce {
            imageClickListener.getImage(imagePath)
        }
    }
}