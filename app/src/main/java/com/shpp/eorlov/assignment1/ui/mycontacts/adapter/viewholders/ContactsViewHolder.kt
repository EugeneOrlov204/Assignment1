package com.shpp.eorlov.assignment1.ui.mycontacts.adapter.viewholders

import android.os.SystemClock
import androidx.core.view.isVisible
import androidx.recyclerview.selection.ItemDetailsLookup
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

    fun bindTo(contact: UserModel, isItemSelected: Boolean = false) {
        this.contact = contact

        contact.apply {
            if (selected) {
                binding.constraintLayoutContactSelectedListItem.isVisible = true
                binding.constraintLayoutContactUnselectedListItem.isVisible = false
                binding.textViewPersonNameSelected.text = name
                binding.textViewPersonProfessionSelected.text = profession
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
                    if (!contact.selected) {
                        onContactClickListener.onContactSelected(contact)
                    } else {
                        checkBoxSelectedState.isChecked = !checkBoxSelectedState.isChecked
                    }
                    previousClickTimestamp = SystemClock.uptimeMillis()
                }
            }

            constraintLayoutContactListItem.setOnLongClickListener {
                if (kotlin.math.abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                    onContactClickListener.onContactsSelected()
                    checkBoxSelectedState.isChecked = true
                    previousClickTimestamp = SystemClock.uptimeMillis()
                }
                true
            }
        }
    }


    fun getItemDetails(): ItemDetailsLookup.ItemDetails<UserModel> =
        object : ItemDetailsLookup.ItemDetails<UserModel>() {
            override fun getPosition(): Int = bindingAdapterPosition
            override fun getSelectionKey(): UserModel = contact
        }
}