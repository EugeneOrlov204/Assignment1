package com.shpp.eorlov.assignment1.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhotoModel(
    @PrimaryKey(autoGenerate = true) val photoId: Int = 0,
    val photoPath: String
)