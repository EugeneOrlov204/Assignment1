package com.shpp.eorlov.assignment1.models

import java.util.*

data class User(
    val email: String,
    val updated_at: Date,
    val created_at: Date,
    val id: Int
)