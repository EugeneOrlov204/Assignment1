package com.shpp.eorlov.assignment1.data.retrofit

import com.shpp.eorlov.assignment1.models.*
import javax.inject.Inject

class MainRemoteData @Inject constructor(private val mainService: MainService) {

    suspend fun registerUser(registerModel: RegisterModel): RegistrationResponseModel {
        with(mainService.registerUser(registerModel)) {

            return body()
                ?: RegistrationResponseModel(
                    this.message(),
                    this.code(),
                    this.message(),
                    null
                )
        }
    }

    suspend fun editUser(editUserModel: EditUserModel, accessToken: String): EditUserResponseModel {
        with(mainService.editUser(editUserModel, accessToken)) {

            return body()
                ?: EditUserResponseModel(
                    this.message(),
                    this.code(),
                    this.message(),
                    null
                )
        }
    }


    suspend fun authorizeUser(authorizeModel: AuthorizeModel): AuthorizationResponseModel {
        with(mainService.authorizeUser(authorizeModel)) {
            return body()
                ?: AuthorizationResponseModel(
                    this.message(),
                    this.code(),
                    this.message(),
                    null
                )
        }
    }

    suspend fun getUser(accessToken: String): GetUserResponseModel? {
        with(mainService.getUser(accessToken)) {
            return body()
        }
    }

    suspend fun getAllUsers(accessToken: String) : GetAllUsersResponseModel? {
        with(mainService.getAllUsers(accessToken)) {
            return body()
        }
    }
}