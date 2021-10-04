package com.shpp.eorlov.assignment1.ui.myContacts.adapter.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.databinding.ContactListItemBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.myContacts.adapter.listeners.ContactClickListener
import com.shpp.eorlov.assignment1.utils.ext.clickWithDebounce

//todo off navigation to detail view and any other fragments
class ContactsViewHolder(
    private val binding: ContactListItemBinding,
    private val onContactClickListener: ContactClickListener,
    private val multiSelect: Boolean,
    private val selectedItems: ArrayList<UserModel>,
    private val addContactsState: Boolean
) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var contact: UserModel

    fun bindTo(contact: UserModel) {
        this.contact = contact

        contact.apply {
            if (multiSelect) {
                binding.constraintLayoutContactListItem.setBackgroundResource(R.drawable.round_view_holder_selected)
                binding.checkBoxSelectedState.visibility = View.VISIBLE
                binding.imageViewRemoveButton.visibility = View.GONE
                if (selectedItems.contains(contact)) {
                    binding.checkBoxSelectedState.isChecked = true
                }
            } else {
                binding.constraintLayoutContactListItem.setBackgroundResource(R.drawable.round_view_holder)
                binding.checkBoxSelectedState.visibility = View.GONE

                if (addContactsState) {
                    binding.imageViewRemoveButton.visibility = View.GONE
                    showAddContactsUI()
                } else {
                    binding.imageViewRemoveButton.visibility = View.VISIBLE
                }
            }

            binding.textViewPersonName.text = name
            binding.textViewPersonProfession.text = career
            binding.draweeViewPersonImage.setImageURI(photo)
        }

        setListeners()
    }

    private fun showAddedUserUI() {
        binding.imageViewAddContact.visibility = View.GONE
        binding.textViewAddContact.visibility = View.GONE
        binding.imageViewCheckedContact.visibility = View.VISIBLE
    }

    private fun showReadyToAddUserUI() {
        binding.imageViewAddContact.visibility = View.VISIBLE
        binding.textViewAddContact.visibility = View.VISIBLE
        binding.imageViewCheckedContact.visibility = View.GONE
    }

    private fun showAddContactsUI() {
        if (!selectedItems.contains(contact)) {
            showReadyToAddUserUI()
        } else {
            showAddedUserUI()
        }
    }


    private fun setListeners() {
        binding.apply {
            imageViewRemoveButton.clickWithDebounce {
                onContactClickListener.onContactRemove(bindingAdapterPosition)
            }

            constraintLayoutContactListItem.clickWithDebounce {
                if (!addContactsState) {
                    if (!multiSelect) {
                        onContactClickListener.onContactClick(contact)
                    } else {
                        checkBoxSelectedState.isChecked = !checkBoxSelectedState.isChecked
                        selectItem(contact)
                        onContactClickListener.onContactSelectedStateChanged()
                    }
                }
            }

            checkBoxSelectedState.setOnClickListener {
                selectItem(contact)
                onContactClickListener.onContactSelectedStateChanged()
            }

            constraintLayoutContactListItem.setOnLongClickListener {
                if (!addContactsState && !multiSelect) {
                    onContactClickListener.onMultiselectActivated()
                    selectItem(contact)
                }
                return@setOnLongClickListener true
            }

            imageViewAddContact.setOnClickListener {
                if (imageViewCheckedContact.visibility != View.VISIBLE) {
                    activateCheckedUserInAddContact()
                }
            }

            textViewAddContact.setOnClickListener {
                if (imageViewCheckedContact.visibility != View.VISIBLE) {
                    activateCheckedUserInAddContact()
                }
            }
        }
    }

    private fun activateCheckedUserInAddContact() {
        if (!selectedItems.contains(contact)) {
            selectedItems.add(contact)
            showAddedUserUI()

            if (selectedItems.size == 1) {
                onContactClickListener.onCheckedContactActivated()
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