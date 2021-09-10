package com.shpp.eorlov.assignment1.api

import com.shpp.eorlov.assignment1.models.*
import javax.inject.Inject

class MainRemoteData @Inject constructor(private val mainService: MainService) {

    suspend fun registerUser(registerModel: RegisterModel): RegistrationResponseModel? {
        with(mainService.registerUser(registerModel)) {
            return body()
        }
    }


    suspend fun authorizeUser(authorizeModel: AuthorizeModel): AuthorizationResponseModel? {
        with(mainService.authorizeUser(authorizeModel)) {
            return body()
        }
    }

    suspend fun getUser(accessToken: String): GetUserResponseModel? {
        with(mainService.getUser(accessToken)) {
            return body()
        }
    }
}