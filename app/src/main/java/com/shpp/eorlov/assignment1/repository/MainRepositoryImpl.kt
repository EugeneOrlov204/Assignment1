package com.shpp.eorlov.assignment1.repository

import com.shpp.eorlov.assignment1.data.retrofit.MainRemoteData
import com.shpp.eorlov.assignment1.model.AuthorizeModel
import com.shpp.eorlov.assignment1.model.EditUserModel
import com.shpp.eorlov.assignment1.model.RegisterModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImpl @Inject constructor(
    private val remoteData: MainRemoteData
) : MainRepository {
    override suspend fun registerUser(registerModel: RegisterModel) =
        remoteData.registerUser(registerModel)

    override suspend fun editUser(editUserModel: EditUserModel, accessToken: String) =
        remoteData.editUser(editUserModel, accessToken)

    override suspend fun authorizeUser(authorizeModel: AuthorizeModel) =
        remoteData.authorizeUser(authorizeModel)

    override suspend fun getUser(accessToken: String) =
        remoteData.getUser(accessToken)

    override suspend fun getAllUsers(accessToken: String) =
        remoteData.getAllUsers(accessToken)
}