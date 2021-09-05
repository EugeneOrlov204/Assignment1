package com.shpp.eorlov.assignment1.db

import com.shpp.eorlov.assignment1.model.UserModel

interface ContactsDatabaseImplementation {
    fun getDefaultUserModel(): UserModel
    fun getUserModelFromStorage(receivedUserModel: UserModel): UserModel
    fun loadPersonData(): MutableList<UserModel>
    fun saveUserModelToStorage(userModel: UserModel, login: String)
}