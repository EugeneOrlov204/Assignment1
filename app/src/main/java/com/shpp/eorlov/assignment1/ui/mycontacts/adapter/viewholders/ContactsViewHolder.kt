package com.shpp.eorlov.assignment1.ui.mycontacts.adapter.viewholders

import android.os.SystemClock
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.shpp.eorlov.assignment1.databinding.ListItemBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.mycontacts.adapter.listeners.ContactClickListener
import com.shpp.eorlov.assignment1.utils.Constants


class ContactsViewHolder(
    private val binding: ViewBinding,
    private val onContactClickListener: ContactClickListener
) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var contact: UserModel

    fun bindTo(contact: UserModel, isActivated: Boolean = false) {
        this.contact = contact
        when (binding) {
            is ListItemBinding -> {
                contact.apply {
                    binding.textViewPersonName.text = name
                    binding.textViewPersonProfession.text = profession
                    binding.draweeViewPersonImage.setImageURI(photo)
                    binding.constraintLayoutContactListItem.isActivated = isActivated
                }
            }
//            is SelectedListItemBinding -> {
//                contact.apply {
//                    binding.textViewPersonName.text = name
//                    binding.textViewPersonProfession.text = profession
//                    binding.draweeViewPersonImage.setImageURI(photo)
//                }
//            }
            else -> {
            }
        }
        setListeners()
    }


    private var previousClickTimestamp = SystemClock.uptimeMillis()

    private fun setListeners() {
        when (binding) {
            is ListItemBinding -> {
                binding.imageViewRemoveButton.setOnClickListener {
                    if (kotlin.math.abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                        onContactClickListener.onContactRemove(bindingAdapterPosition)
                        previousClickTimestamp = SystemClock.uptimeMillis()
                    }
                }

                binding.constraintLayoutContactListItem.setOnClickListener {
                    if (kotlin.math.abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                        onContactClickListener.onContactSelected(contact)
                        previousClickTimestamp = SystemClock.uptimeMillis()
                    }
                }

                binding.constraintLayoutContactListItem.setOnLongClickListener {
                    onContactClickListener.onContactsSelected()
                    true
                }
            }

//            is SelectedListItemBinding -> {
//                binding.constraintLayoutSelectedContactListItem.setOnClickListener {
//                    binding.imageButtonSelectedState.isChecked =
//                        !binding.imageButtonSelectedState.isChecked
//                }
//            }
        }
    }

    fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
        object : ItemDetailsLookup.ItemDetails<Long>() {
            override fun getPosition(): Int = absoluteAdapterPosition
            override fun getSelectionKey(): Long = itemId
        }
}