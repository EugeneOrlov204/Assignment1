package com.shpp.eorlov.assignment1.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class UserModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String?,
    val career: String?,
    val photo: String?,
    val address: String?,
    val birthday: String?,
    val phone: String?,
    val email: String?
) : Parcelable