package com.shpp.eorlov.assignment1.repository

import com.shpp.eorlov.assignment1.api.MainRemoteData
import com.shpp.eorlov.assignment1.api.MainService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val remoteData: MainRemoteData
) {

    suspend fun registerUser(registerModel: MainService.RegisterModel) =
        remoteData.registerUser(registerModel)

    suspend fun authorizeUser(registerModel: MainService.RegisterModel) =
        remoteData.authorizeUser(registerModel)


}