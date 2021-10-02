package com.shpp.eorlov.assignment1.data.retrofit

import com.shpp.eorlov.assignment1.model.*
import retrofit2.Response
import retrofit2.http.*

interface MainService {

    @Headers("Content-Type: application/json")
    @POST("/api/user/register")
    suspend fun registerUser(@Body request: RegisterModel): Response<RegistrationResponseModel>


    @Headers("Content-Type: application/json")
    @POST("/api/user/profile/edit")
    suspend fun editUser(@Body request: EditUserModel, @Header("Authorization") accessToken: String): Response<EditUserResponseModel>


    @Headers("Content-Type: application/json")
    @POST("/api/user/login")
    suspend fun authorizeUser(@Body request: AuthorizeModel): Response<AuthorizationResponseModel>


    @GET("/api/user/profile")
    suspend fun getUser(@Header("Authorization") accessToken: String): Response<GetUserResponseModel>

    @GET("/api/users")
    suspend fun getAllUsers(@Header("Authorization") accessToken: String): Response<GetAllUsersResponseModel>
}