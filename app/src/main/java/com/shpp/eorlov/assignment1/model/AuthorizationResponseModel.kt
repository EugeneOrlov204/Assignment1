package com.shpp.eorlov.assignment1.model

data class AuthorizationResponseModel(
    val status: String,
    val code: Int,
    val message: String,
    val data: Data?
)
