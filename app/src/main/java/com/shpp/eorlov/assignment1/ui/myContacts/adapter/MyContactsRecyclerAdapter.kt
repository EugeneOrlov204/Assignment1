package com.shpp.eorlov.assignment1.ui.myContacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.shpp.eorlov.assignment1.databinding.ContactListItemBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.myContacts.adapter.listeners.ContactClickListener
import com.shpp.eorlov.assignment1.ui.myContacts.adapter.viewHolder.ContactsViewHolder
import com.shpp.eorlov.assignment1.utils.Constants


class MyContactsRecyclerAdapter(
    private val onContactClickListener: ContactClickListener
) : ListAdapter<UserModel, ContactsViewHolder>(ContactItemDiffCallback()) {

    private var multiSelect: Boolean = false

    // Keeps track of all the selected images
    private val selectedItems = arrayListOf<UserModel>()

    private var addContactsState: Boolean = false

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
            selectedItems,
            addContactsState
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
        for (item in selectedItems) {
            onContactClickListener.onContactRemove(item)
        }
        selectedItems.clear()
        multiSelect = false
    }

    fun hideMyContactsUIAndShowAddContactsUI() {
        addContactsState = true
    }

    fun showMyContactsUIAndHideAddContactsUI() {
        addContactsState = false
    }

    //fixme empty list
    fun getAddedItems(): MutableList<UserModel> {
        val addedItems = selectedItems.toMutableList()
        selectedItems.clear()
        return addedItems
    }
}