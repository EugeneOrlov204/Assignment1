package com.shpp.eorlov.assignment1.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shpp.eorlov.assignment1.model.PhotoModel
import com.shpp.eorlov.assignment1.model.UserModel

@Database(entities = [UserModel::class, PhotoModel::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun photoDao(): PhotoDao
}