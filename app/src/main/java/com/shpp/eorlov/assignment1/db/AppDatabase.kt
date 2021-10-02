package com.shpp.eorlov.assignment1.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shpp.eorlov.assignment1.model.UserModel

@Database(entities = [UserModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}