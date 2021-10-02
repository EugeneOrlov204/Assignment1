package com.shpp.eorlov.assignment1.model

data class GetUserResponseModel(
    val status: String,
    val code: Int,
    val message: String,
    val data: Data
)
