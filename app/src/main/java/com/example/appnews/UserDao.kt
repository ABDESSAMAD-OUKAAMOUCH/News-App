package com.example.appnews

import androidx.room.*
import com.example.appnews.Models.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user_table WHERE email = :email")
    suspend fun getUser(email: String): User?

    @Update
    suspend fun updateUser(user: User)
}