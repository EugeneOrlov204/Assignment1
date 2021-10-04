package com.shpp.eorlov.assignment1.ui.myContacts.adapter.listeners

import android.view.View
import com.shpp.eorlov.assignment1.model.UserModel

interface ContactClickListener {
    fun onContactRemove(position: Int)
    fun onContactRemove(userModel: UserModel)
    fun onContactClick(contact: UserModel)
    fun onMultiselectActivated()
    fun onContactSelectedStateChanged()
    fun onCheckedContactActivated()
}