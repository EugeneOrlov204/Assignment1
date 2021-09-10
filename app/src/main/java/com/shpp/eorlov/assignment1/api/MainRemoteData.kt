package com.shpp.eorlov.assignment1.api

import com.google.gson.Gson
import com.shpp.eorlov.assignment1.models.RegistrationResponseModel
import javax.inject.Inject

class MainRemoteData @Inject constructor(private val mainService: MainService) {

    suspend fun registerUser(registerModel: MainService.RegisterModel): String {
        with (mainService.registerUser(registerModel)) {
            return if (isSuccessful) {


                println("Response ${body()?.code}")
                println("Response ${body()?.data?.accessToken}")
                println("Response ${body()?.data?.user}")
                println("Response ${body()?.message}")
                println("Response ${body()?.status}")

                this.body()
                ""
            } else {
                "New user registration error!"
            }
        }
    }



    suspend fun authorizeUser(registerModel: MainService.RegisterModel): String {
        with (mainService.authorizeUser(registerModel)) {
            return if (isSuccessful) {
                this.body()
                ""
            } else {
                "User authorization error!"
            }
        }
    }
}