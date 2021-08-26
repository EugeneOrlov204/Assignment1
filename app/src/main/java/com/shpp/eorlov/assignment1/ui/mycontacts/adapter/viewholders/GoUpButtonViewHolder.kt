package com.shpp.eorlov.assignment1.ui.mycontacts.adapter.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment1.databinding.FragmentGoUpButtonBinding
import com.shpp.eorlov.assignment1.ui.mycontacts.adapter.listeners.ButtonClickListener

class GoUpButtonViewHolder(
    private val binding: FragmentGoUpButtonBinding,
    private val onButtonClickListener: ButtonClickListener
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindTo() {
        setListeners()
    }

    private fun setListeners() {
        binding.buttonGoUp.setOnClickListener {
            onButtonClickListener.onGoUpClicked()
        }
    }
}