package com.shpp.eorlov.assignment1.api

import javax.inject.Inject

class MainRemoteData @Inject constructor(private val mainService: MainService) {

    suspend fun registerUser(postRequest: MainService.PostRequest) =
        mainService.registerUser(postRequest)
}