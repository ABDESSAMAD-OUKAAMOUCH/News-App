package com.example.appnews.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey val email: String,
    var displayName: String?,
    var photoUrl: String?
)
