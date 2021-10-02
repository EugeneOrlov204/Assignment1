package com.shpp.eorlov.assignment1.ui.myContacts.adapter.listeners

import com.shpp.eorlov.assignment1.model.UserModel

interface ContactClickListener {
    fun onContactRemove(position: Int)
    fun onContactRemove(userModel: UserModel)
    fun onContactSelected(contact: UserModel)
    fun onMultiselectActivated()
    fun onContactSelectedStateChanged()
}