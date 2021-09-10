package com.shpp.eorlov.assignment1.api

import com.shpp.eorlov.assignment1.models.*
import retrofit2.Response
import retrofit2.http.*

interface MainService {

    @Headers("Content-Type: application/json")
    @POST("/api/user/register")
    suspend fun registerUser(@Body request: RegisterModel): Response<RegistrationResponseModel>

    @Headers("Content-Type: application/json")
    @POST("/api/user/login")
    suspend fun authorizeUser(@Body request: AuthorizeModel): Response<AuthorizationResponseModel>


    @GET("/api/user/profile")
    suspend fun getUser(@Header("Authorization") accessToken: String): Response<GetUserResponseModel>
}