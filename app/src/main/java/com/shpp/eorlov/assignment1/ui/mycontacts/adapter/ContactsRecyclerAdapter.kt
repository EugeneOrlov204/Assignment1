package com.shpp.eorlov.assignment1.ui.mycontacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment1.databinding.ListItemBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.utils.UserItemDiffCallback


/**
 * Adapter for the [RecyclerView] in [MainActivity]. Displays [UserModel] data object.
 */


class ContactsRecyclerAdapter(
    private val onContactClickListener: ContactClickListener,
) : ListAdapter<UserModel, ContactsViewHolder>(UserItemDiffCallback()) {

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

