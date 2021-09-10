package com.shpp.eorlov.assignment1.ui.mycontacts.adapter.listeners

import com.shpp.eorlov.assignment1.models.UserModel

interface ContactClickListener {
    fun onContactRemove(position: Int)
    fun onContactRemove(userModel: UserModel)
    fun onContactSelected(contact: UserModel)
    fun onMultiselectActivated()
    fun onContactSelectedStateChanged()
}