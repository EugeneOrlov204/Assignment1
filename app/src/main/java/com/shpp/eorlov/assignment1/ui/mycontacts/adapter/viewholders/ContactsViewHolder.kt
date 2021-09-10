package com.shpp.eorlov.assignment1.ui.mycontacts.adapter.viewholders

import android.os.SystemClock
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.databinding.ListItemBinding
import com.shpp.eorlov.assignment1.models.UserModel
import com.shpp.eorlov.assignment1.ui.mycontacts.adapter.listeners.ContactClickListener
import com.shpp.eorlov.assignment1.utils.Constants


class ContactsViewHolder(
    private val binding: ListItemBinding,
    private val onContactClickListener: ContactClickListener,
    private val multiSelect: Boolean,
    private val selectedItems: ArrayList<UserModel>
) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var contact: UserModel

    fun bindTo(contact: UserModel) {
        this.contact = contact

        contact.apply {
            if (multiSelect) {
                binding.constraintLayoutContactListItem.setBackgroundResource(R.drawable.round_view_holder_selected)
                binding.checkBoxSelectedState.visibility = View.VISIBLE
                binding.imageViewRemoveButton.visibility = View.INVISIBLE
            } else {
                binding.constraintLayoutContactListItem.setBackgroundResource(R.drawable.round_view_holder)
                binding.checkBoxSelectedState.visibility = View.GONE
                binding.imageViewRemoveButton.visibility = View.VISIBLE
            }

            if (selectedItems.contains(contact)) {
                binding.checkBoxSelectedState.isChecked = true
            }

            binding.textViewPersonNameUnselected.text = name
            binding.textViewPersonProfessionUnselected.text = profession
            binding.draweeViewPersonImageUnselected.setImageURI(photo)
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
                if (!multiSelect) {
                    if (kotlin.math.abs(SystemClock.uptimeMillis() - previousClickTimestamp) > Constants.BUTTON_CLICK_DELAY) {
                        onContactClickListener.onContactSelected(contact)
                        previousClickTimestamp = SystemClock.uptimeMillis()
                    }
                } else {
                    checkBoxSelectedState.isChecked = !checkBoxSelectedState.isChecked
                    selectItem(contact)
                    onContactClickListener.onContactSelectedStateChanged()
                }
            }

            checkBoxSelectedState.setOnClickListener {
                selectItem(contact)
                onContactClickListener.onContactSelectedStateChanged()
            }

            constraintLayoutContactListItem.setOnLongClickListener {
                if (!multiSelect) {
                    onContactClickListener.onMultiselectActivated()
                    selectItem(contact)
                }
                return@setOnLongClickListener true
            }
        }
    }

    // helper function that adds/removes an item to the list depending on the app's state
    private fun selectItem(userModel: UserModel) {
        if (selectedItems.contains(userModel)) {
            selectedItems.remove(userModel)
            binding.checkBoxSelectedState.isChecked = false
        } else {
            selectedItems.add(userModel)
            binding.checkBoxSelectedState.isChecked = true
        }
    }
}