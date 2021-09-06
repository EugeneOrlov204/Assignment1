package com.shpp.eorlov.assignment1.repository

import com.shpp.eorlov.assignment1.api.MainRemoteData
import com.shpp.eorlov.assignment1.api.MainService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val remoteData: MainRemoteData
) {

    suspend fun registerUser(postRequest: MainService.PostRequest) =
        remoteData.registerUser(postRequest)
}