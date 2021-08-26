package com.shpp.eorlov.assignment1.ui.mycontacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment1.databinding.FragmentGoUpButtonBinding
import com.shpp.eorlov.assignment1.databinding.FragmentRemoveSelectedContactsButtonBinding
import com.shpp.eorlov.assignment1.databinding.ListItemBinding
import com.shpp.eorlov.assignment1.databinding.SelectedListItemBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.mycontacts.adapter.listeners.ContactClickListener
import com.shpp.eorlov.assignment1.ui.mycontacts.adapter.listeners.ButtonClickListener
import com.shpp.eorlov.assignment1.ui.mycontacts.adapter.viewholders.GoUpButtonViewHolder
import com.shpp.eorlov.assignment1.ui.mycontacts.adapter.viewholders.ContactsViewHolder
import com.shpp.eorlov.assignment1.ui.mycontacts.adapter.viewholders.RemoveButtonViewHolder
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.UserItemDiffCallback


class ContactsRecyclerAdapter(
    private val onContactClickListener: ContactClickListener,
    private val onButtonClickListener: ButtonClickListener
) : ListAdapter<UserModel, RecyclerView.ViewHolder>(UserItemDiffCallback()) {

    var isSelected = false


    //fixme bug with removing 6 element

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (!isSelected) {

            return if (viewType == Constants.CONTACT_VIEW_HOLDER_TYPE_CODE) {
                ContactsViewHolder(
                    ListItemBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                    onContactClickListener
                )
            } else {
                GoUpButtonViewHolder(
                    FragmentGoUpButtonBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                    onButtonClickListener
                )
            }
        } else {
            return if (viewType == Constants.CONTACT_VIEW_HOLDER_TYPE_CODE) {
                ContactsViewHolder(
                    SelectedListItemBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                    onContactClickListener
                )
            } else {
                RemoveButtonViewHolder(
                    FragmentRemoveSelectedContactsButtonBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                    onButtonClickListener
                )
            }
        }
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            Constants.CONTACT_VIEW_HOLDER_TYPE_CODE -> {
                (holder as ContactsViewHolder).bindTo(getItem(position))
            }
            Constants.REMOVE_CONTACTS_BUTTON_VIEW_HOLDER_TYPE_CODE -> {
                (holder as RemoveButtonViewHolder).bindTo()
            }
            Constants.GO_UP_BUTTON_VIEW_HOLDER_TYPE_CODE -> {
                (holder as GoUpButtonViewHolder).bindTo()
            }
            else -> {
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position != itemCount - 1 || itemCount <= Constants.MAX_AMOUNT_OF_CONTACTS_IN_SCREEN) {
            Constants.CONTACT_VIEW_HOLDER_TYPE_CODE
        } else if (isSelected) {
            Constants.REMOVE_CONTACTS_BUTTON_VIEW_HOLDER_TYPE_CODE
        } else {
            Constants.GO_UP_BUTTON_VIEW_HOLDER_TYPE_CODE
        }
    }
}


