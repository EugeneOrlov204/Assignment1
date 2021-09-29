package com.shpp.eorlov.assignment1.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val name: String?,
    val career: String?,
    val photo: String?,
    val residenceAddress: String?,
    val birthDate: String?,
    val phoneNumber: String?,
    val email: String?
) : Parcelable

//"name":null,"email":"eugene.crabs3@gmail.com","phone":null,"career":null,"address":null,"birthday"