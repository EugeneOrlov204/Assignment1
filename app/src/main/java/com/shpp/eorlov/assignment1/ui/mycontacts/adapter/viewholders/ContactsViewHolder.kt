package com.shpp.eorlov.assignment1.ui.mycontacts.adapter.viewholders

import android.os.SystemClock
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment1.databinding.ListItemBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.mycontacts.adapter.listeners.ContactClickListener
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
            if (onMultiSelect) {
                binding.constraintLayoutContactSelectedListItem.isVisible = true
                binding.constraintLayoutContactUnselectedListItem.isVisible = false
                binding.textViewPersonNameSelected.text = name
                binding.textViewPersonProfessionSelected.text = profession
                binding.checkBoxSelectedState.isChecked = selected
                binding.draweeViewPersonImageSelected.setImageURI(photo)
            } else {
                binding.constraintLayoutContactSelectedListItem.isVisible = false
                binding.constraintLayoutContactUnselectedListItem.isVisible = true
                binding.textViewPersonNameUnselected.text = name
                binding.textViewPersonProfessionUnselected.text = profession
                binding.draweeViewPersonImageUnselected.setImageURI(photo)
            }
        }

        setListeners()
    }


    private var previousClickTimestamp = SystemClock.uptimeMillis()

    private fun setListeners() {
        binding.apply {
            imageViewRemoveButton.setOnClickListener {
                if (kotlin.math.abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                    onContactClickListener.onContactRemove(bindingAdapterPosition)
                    previousClickTimestamp = SystemClock.uptimeMillis()
                }
            }

            constraintLayoutContactListItem.setOnClickListener {
                if (kotlin.math.abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                    if (!contact.onMultiSelect) {
                        onContactClickListener.onContactSelected(contact)
                        previousClickTimestamp = SystemClock.uptimeMillis()
                    } else {
                        checkBoxSelectedState.isChecked = !checkBoxSelectedState.isChecked
                        contact.selected = !contact.selected
                    }
                }
            }

            constraintLayoutContactListItem.setOnLongClickListener {
                onContactClickListener.onContactsSelected()
                contact.selected = true
                return@setOnLongClickListener true
            }
        }
    }
}