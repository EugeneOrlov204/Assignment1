package com.shpp.eorlov.assignment1.ui.addContacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.shpp.eorlov.assignment1.databinding.LayoutAddContactListItemBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.addContacts.adapter.listeners.IAddContactClickListener
import com.shpp.eorlov.assignment1.ui.addContacts.adapter.viewHolder.AddContactsViewHolder
import com.shpp.eorlov.assignment1.utils.ContactItemDiffCallback

class AddContactsListAdapter(
    private val contactClickListener: IAddContactClickListener,
    private val selectedItems: ArrayList<UserModel>
) : ListAdapter<UserModel, AddContactsViewHolder>(
    ContactItemDiffCallback()
) {

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddContactsViewHolder {
        return AddContactsViewHolder(
            LayoutAddContactListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            contactClickListener,
            selectedItems
        )
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: AddContactsViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun getItemId(position: Int): Long = position.toLong()


    fun getAddedItems(): MutableList<UserModel> {
        return selectedItems.toMutableList()
    }

}