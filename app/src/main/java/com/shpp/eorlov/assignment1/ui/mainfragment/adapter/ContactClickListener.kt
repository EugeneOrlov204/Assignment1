package com.shpp.eorlov.assignment1.ui.mainfragment.adapter

import com.shpp.eorlov.assignment1.model.UserModel

interface ContactClickListener {
    fun onContactRemove(position: Int)
    fun onContactSelected(contact: UserModel)
}