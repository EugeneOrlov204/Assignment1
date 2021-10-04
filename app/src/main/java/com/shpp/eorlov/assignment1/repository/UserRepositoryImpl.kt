package com.shpp.eorlov.assignment1.repository

import com.shpp.eorlov.assignment1.db.UserDao
import com.shpp.eorlov.assignment1.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDao: UserDao) :
    UserRepository {
    override suspend fun getAll(): List<UserModel> {
        var listOfWeathers: List<UserModel>
        withContext(Dispatchers.IO) {
            listOfWeathers = userDao.getAll()
        }
        return listOfWeathers
    }

    override suspend fun insertAll(vararg arrayOfUserModels: UserModel) {
        userDao.insertAll(*arrayOfUserModels)
    }

    override suspend fun delete(userModel: UserModel) {
        userDao.delete(userModel)
    }

    override suspend fun clearTable() {
        userDao.clearTable()
    }
}