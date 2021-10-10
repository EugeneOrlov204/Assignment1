package com.shpp.eorlov.assignment1.repository

import com.shpp.eorlov.assignment1.model.*
import retrofit2.Response

interface MainRepository {
    suspend fun registerUser(registerModel: RegisterModel): Response<ResponseModel<Data?>>
    suspend fun editUser(editUserModel: EditUserModel, accessToken: String): Response<ResponseModel<Data?>>
    suspend fun authorizeUser(authorizeModel: AuthorizeModel): Response<ResponseModel<Data>>
    suspend fun getUser(accessToken: String): Response<ResponseModel<Data>>
    suspend fun getAllUsers(accessToken: String): Response<ResponseModel<DataUsers>>
}