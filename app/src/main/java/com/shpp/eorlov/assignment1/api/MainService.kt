package com.shpp.eorlov.assignment1.api

import com.shpp.eorlov.assignment1.models.RegistrationResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MainService {

    data class RegisterModel(
        val email: String,
        val password: String
    )

    @Headers("Content-Type: application/json")
    @POST("/api/user/register")
    suspend fun registerUser(@Body request: RegisterModel): Response<RegistrationResponseModel>

    @Headers("Content-Type: application/json")
    @POST("/api/user/login")
    suspend fun authorizeUser(@Body request: RegisterModel): Response<Any>
}