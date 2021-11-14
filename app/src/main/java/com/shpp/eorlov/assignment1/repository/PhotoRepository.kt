package com.shpp.eorlov.assignment1.repository

import com.shpp.eorlov.assignment1.model.PhotoModel

interface PhotoRepository {
    suspend fun insertAll(vararg arrayOfPhotoModels: PhotoModel)
    suspend fun getAllPhotos(): List<PhotoModel>
    suspend fun removeItem(photoModel: PhotoModel)
    suspend fun clearTable()
}