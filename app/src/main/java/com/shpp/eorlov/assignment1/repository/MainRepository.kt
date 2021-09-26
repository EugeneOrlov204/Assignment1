package com.shpp.eorlov.assignment1.repository

import com.shpp.eorlov.assignment1.data.retrofit.MainRemoteData
import com.shpp.eorlov.assignment1.models.AuthorizeModel
import com.shpp.eorlov.assignment1.models.EditUserModel
import com.shpp.eorlov.assignment1.models.RegisterModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val remoteData: MainRemoteData
) {

    suspend fun registerUser(registerModel: RegisterModel) =
        remoteData.registerUser(registerModel)

    suspend fun editUser(editUserModel: EditUserModel, accessToken: String) =
        remoteData.editUser(editUserModel, accessToken)

    suspend fun authorizeUser(authorizeModel: AuthorizeModel) =
        remoteData.authorizeUser(authorizeModel)

    suspend fun getUser(accessToken: String) =
        remoteData.getUser(accessToken)
}