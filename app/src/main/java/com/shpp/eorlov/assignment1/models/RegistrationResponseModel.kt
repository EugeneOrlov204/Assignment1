package com.shpp.eorlov.assignment1.models

import java.util.*

data class RegistrationResponseModel(
    val status: String,
    val code: Int,
    val message: String,
    val data: Data
)

data class Data(
    val user: User,
    val accessToken: String
)

data class User(
    val email: String,
    val updated_at: Date,
    val created_at: Date,
    val id: Int
)

