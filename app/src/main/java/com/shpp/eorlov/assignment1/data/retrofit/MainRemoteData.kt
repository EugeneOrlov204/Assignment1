package com.shpp.eorlov.assignment1.data.retrofit

import com.shpp.eorlov.assignment1.model.*
import retrofit2.Response
import javax.inject.Inject

class MainRemoteData @Inject constructor(private val mainService: MainService) {

    suspend fun registerUser(registerModel: RegisterModel) = mainService.registerUser(registerModel)


    suspend fun editUser(editUserModel: EditUserModel, accessToken: String) =
        mainService.editUser(editUserModel, accessToken)


    suspend fun authorizeUser(authorizeModel: AuthorizeModel) =
        mainService.authorizeUser(authorizeModel)


    suspend fun getUser(accessToken: String) = mainService.getUser(accessToken)

    suspend fun getAllUsers(accessToken: String) =
        mainService.getAllUsers(accessToken)

}