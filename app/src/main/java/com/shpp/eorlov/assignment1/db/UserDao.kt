package com.shpp.eorlov.assignment1.db

import androidx.room.*
import com.shpp.eorlov.assignment1.model.UserModel

@Dao
interface UserDao {
    @Query("SELECT * FROM usermodel")
    suspend fun getAll(): List<UserModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg arrayOfUserModels: UserModel)

    @Delete
    suspend fun delete(userModel: UserModel)
}