package com.shpp.eorlov.assignment1.model

data class ResponseModel<T> (
    val code: Int,
    val data: T,
    val message: String,
    val status: String
)