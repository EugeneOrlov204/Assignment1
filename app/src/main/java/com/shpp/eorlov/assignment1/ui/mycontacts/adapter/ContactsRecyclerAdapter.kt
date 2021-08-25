package com.shpp.eorlov.assignment1.ui.mycontacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.shpp.eorlov.assignment1.databinding.ListItemBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.utils.UserItemDiffCallback


class ContactsRecyclerAdapter(
    private val onContactClickListener: ContactClickListener,
) : ListAdapter<UserModel, ContactsViewHolder>(UserItemDiffCallback()) {

    //todo implement https://stackoverflow.com/questions/26245139/how-to-create-recyclerview-with-multiple-view-types
    //fixme remove white background in go up button

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        return ContactsViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            onContactClickListener
        )
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }
}

