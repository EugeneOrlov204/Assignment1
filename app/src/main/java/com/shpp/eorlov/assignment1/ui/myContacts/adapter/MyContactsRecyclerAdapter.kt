package com.shpp.eorlov.assignment1.ui.myContacts.adapter

import android.R
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.shpp.eorlov.assignment1.databinding.LayoutContactListItemBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.myContacts.adapter.listeners.ContactClickListener
import com.shpp.eorlov.assignment1.ui.myContacts.adapter.viewHolder.ContactsViewHolder
import com.shpp.eorlov.assignment1.utils.ContactItemDiffCallback


class MyContactsRecyclerAdapter(
    private val onContactClickListener: ContactClickListener
) : ListAdapter<UserModel, ContactsViewHolder>(ContactItemDiffCallback()) {

    private var multiSelect: Boolean = false

    // Keeps track of all the selected images
    private val selectedItems = arrayListOf<UserModel>()

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        return ContactsViewHolder(
            LayoutContactListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            onContactClickListener,
            multiSelect,
            selectedItems
        )
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun getItemId(position: Int): Long = position.toLong()

    fun isMultiSelect(): Boolean = multiSelect

    fun areAllItemsUnselected(): Boolean {
        if (selectedItems.isEmpty()) {
            multiSelect = false
            return true
        }
        return false
    }

    fun switchToMultiSelect() {
        multiSelect = true
    }

    fun removeSelectedItems() {
        for (item in selectedItems) {
            onContactClickListener.onContactRemove(item)
        }
        selectedItems.clear()
        multiSelect = false
    }

}