package com.shpp.eorlov.assignment1.repository

import com.shpp.eorlov.assignment1.db.PhotoDao
import com.shpp.eorlov.assignment1.model.PhotoModel
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(private val photoDao: PhotoDao) : PhotoRepository {
    override suspend fun insertAll(vararg arrayOfPhotoModels: PhotoModel) {
        photoDao.insertAll(*arrayOfPhotoModels)
    }

    override suspend fun getAllPhotos() : List<PhotoModel>{
       return photoDao.getAllPhotos()
    }

    override suspend fun removeItem(photoModel: PhotoModel) {
        photoDao.removeItem(photoModel.photoPath)
    }
    override suspend fun clearTable() {
        photoDao.clearTable()
    }
}