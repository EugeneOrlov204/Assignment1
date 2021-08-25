package com.shpp.eorlov.assignment1.ui.mycontacts.adapter

import android.os.SystemClock
import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment1.databinding.ListItemBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.utils.Constants


class ContactsViewHolder(
    private val binding: ListItemBinding,
    private val onContactClickListener: ContactClickListener
) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var contact: UserModel

    fun bindTo(contact: UserModel) {
        this.contact = contact
        contact.apply {
            binding.textViewPersonName.text = name
            binding.textViewPersonProfession.text = profession
            binding.draweeViewPersonImage.setImageURI(photo)
            setListeners()
        }
    }


    private var previousClickTimestamp = SystemClock.uptimeMillis()

    private fun setListeners() {
        binding.imageViewRemoveButton.setOnClickListener {
            if (kotlin.math.abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                onContactClickListener.onContactRemove(bindingAdapterPosition)
                previousClickTimestamp = SystemClock.uptimeMillis()
            }
        }


        binding.constraintLayoutContact.setOnClickListener {
            if (kotlin.math.abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                onContactClickListener.onContactSelected(contact)
                previousClickTimestamp = SystemClock.uptimeMillis()

            }
        }
    }
}