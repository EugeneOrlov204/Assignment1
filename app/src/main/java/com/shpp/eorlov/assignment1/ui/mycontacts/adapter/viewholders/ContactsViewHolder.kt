package com.shpp.eorlov.assignment1.ui.mycontacts.adapter.viewholders

import android.os.SystemClock
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment1.R
import com.shpp.eorlov.assignment1.databinding.ListItemBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.mycontacts.adapter.listeners.ContactClickListener
import com.shpp.eorlov.assignment1.utils.Constants


class ContactsViewHolder(
    private val binding: ListItemBinding,
    private val onContactClickListener: ContactClickListener,
    private val multiSelect: Boolean,
    private val selectedItems: ArrayList<UserModel>
) :
    RecyclerView.ViewHolder(binding.root) {

    //fixme bug with checkbox

    private lateinit var contact: UserModel

    fun bindTo(contact: UserModel) {
        this.contact = contact

        contact.apply {
            if (multiSelect) {
                binding.constraintLayoutContactListItem.setBackgroundResource(R.drawable.round_view_holder_selected)
                binding.checkBoxSelectedState.visibility = View.VISIBLE
            } else {
                binding.constraintLayoutContactListItem.setBackgroundResource(R.drawable.round_view_holder)
                binding.textViewPersonNameUnselected.text = name
                binding.textViewPersonProfessionUnselected.text = profession
                binding.draweeViewPersonImageUnselected.setImageURI(photo)
                binding.checkBoxSelectedState.visibility = View.GONE
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



            constraintLayoutContactListItem.setOnLongClickListener {

                return@setOnLongClickListener true
            }
        }
    }
}