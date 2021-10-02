package com.shpp.eorlov.assignment1.repository

import com.shpp.eorlov.assignment1.model.UserModel

interface UserRepository {
    suspend fun getAll() : List<UserModel>
    suspend fun insertAll(vararg arrayOfUserModels: UserModel)
    suspend fun delete(userModel: UserModel)
}