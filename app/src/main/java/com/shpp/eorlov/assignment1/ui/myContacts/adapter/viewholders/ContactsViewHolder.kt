package com.shpp.eorlov.assignment1.ui.myContacts.adapter.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.databinding.ContactListItemBinding
import com.shpp.eorlov.assignment1.models.UserModel
import com.shpp.eorlov.assignment1.ui.myContacts.adapter.listeners.ContactClickListener
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce


class ContactsViewHolder(
    private val binding: ContactListItemBinding,
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

            binding.textViewPersonName.text = name
            binding.textViewPersonProfession.text = career
            binding.draweeViewPersonImage.setImageURI(photo)
        }

        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            imageViewRemoveButton.clickWithDebounce {
                onContactClickListener.onContactRemove(bindingAdapterPosition)
            }

            constraintLayoutContactListItem.clickWithDebounce {
                if (!multiSelect) {
                    onContactClickListener.onContactSelected(contact) } else {
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