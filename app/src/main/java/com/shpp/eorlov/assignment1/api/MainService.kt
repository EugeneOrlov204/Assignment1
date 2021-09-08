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

    data class ResponseWrapper<T>(
        val code: Int,
        val message: String?,
        val status: String
    )

    enum class Status(val value: String) {
        SUCCESS("success"),
        ERROR("error")
    }

    enum class Code(val value: Int) {
        SUCCESS(200),
        BAD_REQUEST(400),
        UNAUTHORIZED(401),
        FORBIDDEN(403),
        NOT_FOUND(404)
    }

    @Headers("Content-Type: application/json")
    @POST("/api/user/register")
    suspend fun registerUser(@Body request: RegisterModel): Response<Any>
}