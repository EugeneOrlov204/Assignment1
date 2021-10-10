package com.shpp.eorlov.assignment1.ui.myContacts.adapter.listeners

import com.shpp.eorlov.assignment1.model.UserModel

interface ContactClickListener {
    fun onContactRemove(userModel: UserModel)
    fun onContactClick(contact: UserModel)
    fun onMultiSelectEnabled()
    fun onContactSelectedStateChanged()
}