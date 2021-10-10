package com.shpp.eorlov.assignment1.ui.addContacts.adapter.listeners

import com.shpp.eorlov.assignment1.model.UserModel

interface IAddContactClickListener {
    fun onCheckedContacts(contact: UserModel)
}