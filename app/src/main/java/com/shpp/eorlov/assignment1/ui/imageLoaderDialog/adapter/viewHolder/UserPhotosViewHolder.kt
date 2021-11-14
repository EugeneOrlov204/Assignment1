package com.shpp.eorlov.assignment1.ui.imageLoaderDialog.adapter.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment1.databinding.PhotoListItemBinding
import com.shpp.eorlov.assignment1.ui.imageLoaderDialog.adapter.listeners.ImageClickListener
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce
import com.shpp.eorlov.assignment1.utils.ext.loadCircleImage
import com.shpp.eorlov.assignment1.utils.ext.loadImage

class UserPhotosViewHolder constructor(
    private val binding: PhotoListItemBinding,
    private val imageClickListener: ImageClickListener
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindTo(imagePath: String) {
        if(imagePath.isNotEmpty()) {
            binding.imageViewUserImage.loadImage(imagePath)
        }

        setListeners(imagePath)
    }

    private fun setListeners(imagePath: String) {
        binding.constraintLayoutPhotoListItem.clickWithDebounce {
            if(bindingAdapterPosition == 0) {
                takePicture()
            } else {
                returnImageToParent(imagePath)
            }
        }
    }

    private fun takePicture() {
        imageClickListener.takePicture()
    }

    private fun returnImageToParent(imagePath: String) {
        imageClickListener.getImage(imagePath)
    }
}