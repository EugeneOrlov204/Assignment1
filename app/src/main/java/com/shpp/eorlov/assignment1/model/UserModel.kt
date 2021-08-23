package com.shpp.eorlov.assignment1.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var id: Int,
    val name: String,
    val profession: String,
    val photo: String,
    val residenceAddress: String,
    val birthDate: String,
    val phoneNumber: String,
    val email: String
) : Parcelable
