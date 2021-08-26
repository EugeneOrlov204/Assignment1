package com.shpp.eorlov.assignment1.ui.mycontacts.adapter.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment1.databinding.FragmentGoUpButtonBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.mycontacts.adapter.listeners.GoUpButtonClickListener

class ButtonViewHolder(
    private val binding: FragmentGoUpButtonBinding,
    private val onGoUpButtonClickListener: GoUpButtonClickListener
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindTo() {
        setListeners()
    }

    private fun setListeners() {
        binding.buttonGoUp.setOnClickListener {
            onGoUpButtonClickListener.onGoUpClicked()
        }
    }
}