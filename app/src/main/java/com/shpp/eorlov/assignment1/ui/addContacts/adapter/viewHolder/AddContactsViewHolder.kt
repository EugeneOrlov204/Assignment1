package com.shpp.eorlov.assignment1.ui.addContacts.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.databinding.LayoutAddContactListItemBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.addContacts.adapter.listeners.IAddContactClickListener
import com.shpp.eorlov.assignment1.utils.ext.gone
import com.shpp.eorlov.assignment1.utils.ext.visible

class AddContactsViewHolder(
    private val binding: LayoutAddContactListItemBinding,
    private val contactClickListener: IAddContactClickListener,
    private val selectedItems: ArrayList<UserModel>
) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var contact: UserModel

    fun bindTo(contact: UserModel) {
        this.contact = contact

        contact.apply {
            binding.constraintLayoutContactListItem.setBackgroundResource(R.drawable.round_view_holder)
            binding.textViewPersonName.text = name
            binding.textViewPersonProfession.text = career
            binding.draweeViewPersonImage.setImageURI(photo)
            showAddContactsUI()
        }

        setListeners()
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

    private fun showAddedUserUI() {
        binding.imageViewAddContact.gone()
        binding.textViewAddContact.gone()
        binding.imageViewCheckedContact.visible()
    }

    private fun showReadyToAddUserUI() {
        binding.imageViewAddContact.visible()
        binding.textViewAddContact.visible()
        binding.imageViewCheckedContact.gone()
    }

    private fun activateCheckedUserInAddContact() {
        if (!selectedItems.contains(contact)) {
            selectedItems.add(contact)
            showAddedUserUI()
            if(selectedItems.size == 1) {
                contactClickListener.onCheckedContacts()
            }
        }
    }
}