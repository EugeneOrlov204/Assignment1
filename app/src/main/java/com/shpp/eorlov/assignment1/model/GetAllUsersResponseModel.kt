package com.shpp.eorlov.assignment1.model

data class GetAllUsersResponseModel(
    val status: String,
    val code: Int,
    val message: String,
    val data: AllUsersData
)

data class AllUsersData(
    val users: ArrayList<UserModel>
)