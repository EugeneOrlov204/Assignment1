package com.shpp.eorlov.assignment1.api

import javax.inject.Inject

class MainRemoteData @Inject constructor(private val mainService: MainService) {

    suspend fun registerUser(registerModel: MainService.RegisterModel): String {
        with (mainService.registerUser(registerModel)) {
            return if (isSuccessful) {
                this.message()
            } else {
                errorBody().toString()
            }
        }
    }
}