package com.shpp.eorlov.assignment1.db

import com.shpp.eorlov.assignment1.model.UserModel

interface LocalDB {
    fun getDefaultUserModel(): UserModel
    fun getUserModelFromStorage(): UserModel
    fun loadPersonData(): MutableList<UserModel>
    fun saveUserModelToStorage(userModel: UserModel)
}