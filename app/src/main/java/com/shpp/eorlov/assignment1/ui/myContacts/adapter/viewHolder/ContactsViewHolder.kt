package com.shpp.eorlov.assignment1.ui.myContacts.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.databinding.LayoutContactListItemBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.myContacts.adapter.listeners.ContactClickListener
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce

class ContactsViewHolder(
    private val binding: LayoutContactListItemBinding,
    private val contactClickListener: ContactClickListener,
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
                binding.imageViewRemoveButton.visibility = View.GONE
                binding.checkBoxSelectedState.visibility = View.VISIBLE
                if (selectedItems.contains(contact)) {
                    binding.checkBoxSelectedState.isChecked = true
                }
            } else {
                binding.constraintLayoutContactListItem.setBackgroundResource(R.drawable.round_view_holder)
                binding.checkBoxSelectedState.visibility = View.GONE
                binding.imageViewRemoveButton.visibility = View.VISIBLE
            }

            binding.textViewPersonName.text = name
            binding.textViewPersonProfession.text = career
            binding.draweeViewPersonImage.setImageURI(image)
        }

        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            imageViewRemoveButton.clickWithDebounce {
                contactClickListener.onContactRemove(contact)
            }

            constraintLayoutContactListItem.clickWithDebounce {
                if (!multiSelect) {
                    contactClickListener.onContactClick(contact)
                } else {
                    checkBoxSelectedState.isChecked = !checkBoxSelectedState.isChecked
                    selectItemToRemove(contact)
                    contactClickListener.onContactSelectedStateChanged()
                }
            }

            checkBoxSelectedState.setOnClickListener {
                selectItemToRemove(contact)
                contactClickListener.onContactSelectedStateChanged()
            }

            constraintLayoutContactListItem.setOnLongClickListener {
                if (!multiSelect) {
                    contactClickListener.onMultiSelectEnabled()
                    selectItemToRemove(contact)
                }
                return@setOnLongClickListener true
            }
        }
    }

    private fun selectItemToRemove(userModel: UserModel) {
        if (selectedItems.contains(userModel)) {
            selectedItems.remove(userModel)
            binding.checkBoxSelectedState.isChecked = false
        } else {
            selectedItems.add(userModel)
            binding.checkBoxSelectedState.isChecked = true
        }
    }
}