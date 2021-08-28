package com.shpp.eorlov.assignment1.ui.mycontacts

import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shpp.eorlov.assignment1.model.UserModel
import com.shpp.eorlov.assignment1.ui.mycontacts.adapter.ContactsRecyclerAdapter

class MyItemKeyProvider(private val adapter: ContactsRecyclerAdapter) :
    ItemKeyProvider<UserModel>(SCOPE_CACHED) {

    override fun getKey(position: Int): UserModel =
        adapter.currentList[position]

    override fun getPosition(key: UserModel): Int =
        adapter.currentList.indexOfFirst { it == key }
}