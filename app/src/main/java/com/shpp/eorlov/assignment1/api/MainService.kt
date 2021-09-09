package com.shpp.eorlov.assignment1.api

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
    suspend fun registerUser(@Body request: RegisterModel): Response<Any>
}