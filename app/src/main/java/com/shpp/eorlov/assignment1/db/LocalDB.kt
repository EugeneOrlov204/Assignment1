package com.shpp.eorlov.assignment1.db

import com.shpp.eorlov.assignment1.model.UserModel

interface LocalDB {
    fun getDefaultUserModel(): UserModel
    fun loadPersonData(): MutableList<UserModel>
}