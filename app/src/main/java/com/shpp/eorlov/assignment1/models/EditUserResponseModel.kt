package com.shpp.eorlov.assignment1.models

data class EditUserResponseModel(
    val status: String,
    val code: Int,
    val message: String,
    val data: Data?
)
