package com.shpp.eorlov.assignment1.ui.mycontacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.shpp.eorlov.assignment1.databinding.ContactListItemBinding
import com.shpp.eorlov.assignment1.models.UserModel
import com.shpp.eorlov.assignment1.ui.mycontacts.adapter.listeners.ContactClickListener
import com.shpp.eorlov.assignment1.ui.mycontacts.adapter.viewholders.ContactsViewHolder
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.ContactItemDiffCallback


class ContactsRecyclerAdapter(
    private val onContactClickListener: ContactClickListener,
    private var multiSelect: Boolean = false
) : ListAdapter<UserModel, ContactsViewHolder>(ContactItemDiffCallback()) {


    // Keeps track of all the selected images
    private val selectedItems = arrayListOf<UserModel>()

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        return ContactsViewHolder(
            ContactListItemBinding.inflate(
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


    override fun getItemViewType(position: Int): Int = Constants.CONTACT_VIEW_HOLDER_TYPE_CODE


    override fun getItemId(position: Int): Long = position.toLong()

    fun isMultiSelect(): Boolean = multiSelect

    fun areAllItemsUnselected(): Boolean {
        if (selectedItems.isEmpty()) {
            multiSelect = false
            return true
        }
        return false
    }

    fun selectAllContacts() {
        multiSelect = true
    }

    fun removeSelectedItems() {
        for(item in selectedItems) {
            onContactClickListener.onContactRemove(item)
        }
        selectedItems.clear()
        multiSelect = false
    }
}