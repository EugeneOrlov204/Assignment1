package com.shpp.eorlov.assignment1.api

import com.shpp.eorlov.assignment1.models.AuthorizationResponseModel
import com.shpp.eorlov.assignment1.models.AuthorizeModel
import com.shpp.eorlov.assignment1.models.RegisterModel
import com.shpp.eorlov.assignment1.models.RegistrationResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MainService {

    @Headers("Content-Type: application/json")
    @POST("/api/user/register")
    suspend fun registerUser(@Body request: RegisterModel): Response<RegistrationResponseModel>

    @Headers("Content-Type: application/json")
    @POST("/api/user/login")
    suspend fun authorizeUser(@Body request: AuthorizeModel): Response<AuthorizationResponseModel>
}