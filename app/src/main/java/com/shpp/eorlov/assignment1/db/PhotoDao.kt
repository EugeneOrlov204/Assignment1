package com.shpp.eorlov.assignment1.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shpp.eorlov.assignment1.model.PhotoModel

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg arrayOfPhotoModels: PhotoModel)

    @Query("SELECT * FROM photomodel")
    suspend fun getAllPhotos(): List<PhotoModel>

    @Query("DELETE FROM photomodel WHERE photoPath = :photoPath")
    suspend fun removeItem(photoPath: String)

    @Query("DELETE FROM photomodel")
    suspend fun clearTable()
}