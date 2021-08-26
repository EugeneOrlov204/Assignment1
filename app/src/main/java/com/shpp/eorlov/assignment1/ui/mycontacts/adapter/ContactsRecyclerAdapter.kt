package com.shpp.eorlov.assignment1.ui.mycontacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment1.databinding.FragmentGoUpButtonBinding
import com.shpp.eorlov.assignment1.databinding.ListItemBinding
import com.shpp.eorlov.assignment1.databinding.SelectedListItemBinding
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.mycontacts.adapter.listeners.ContactClickListener
import com.shpp.eorlov.assignment1.ui.mycontacts.adapter.listeners.GoUpButtonClickListener
import com.shpp.eorlov.assignment1.ui.mycontacts.adapter.viewholders.ButtonViewHolder
import com.shpp.eorlov.assignment1.ui.mycontacts.adapter.viewholders.ContactsViewHolder
import com.shpp.eorlov.assignment1.utils.Constants
import com.shpp.eorlov.assignment1.utils.UserItemDiffCallback


class ContactsRecyclerAdapter(
    private val onContactClickListener: ContactClickListener,
    private val onGoUpButtonClickListener: GoUpButtonClickListener
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
                ButtonViewHolder(
                    FragmentGoUpButtonBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                    onGoUpButtonClickListener
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
                ButtonViewHolder(
                    FragmentGoUpButtonBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                    onGoUpButtonClickListener
                )
            }
        }
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == Constants.CONTACT_VIEW_HOLDER_TYPE_CODE) {
            (holder as ContactsViewHolder).bindTo(getItem(position))
        } else {
            (holder as ButtonViewHolder).bindTo()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position != itemCount - 1 || itemCount <= Constants.MAX_AMOUNT_OF_CONTACTS_IN_SCREEN) {
            Constants.CONTACT_VIEW_HOLDER_TYPE_CODE
        } else {
            Constants.BUTTON_VIEW_HOLDER_TYPE_CODE
        }
    }
}


