package com.shpp.eorlov.assignment1.ui.mycontacts.adapter.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment1.databinding.FragmentRemoveSelectedContactsButtonBinding
import com.shpp.eorlov.assignment1.ui.mycontacts.adapter.listeners.ButtonClickListener

class RemoveButtonViewHolder(
    private val binding: FragmentRemoveSelectedContactsButtonBinding,
    private val onButtonClickListener: ButtonClickListener
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindTo() {
        setListeners()
    }

    private fun setListeners() {
        binding.buttonRemoveSelectedContacts.setOnClickListener {
            onButtonClickListener.onRemoveSelectedContactsClicked()
        }
    }
}