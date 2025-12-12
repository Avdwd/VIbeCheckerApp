package com.example.vibechecker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey
    val id: Int = 1,

    val name: String = "User",
    val age: String = "",
    val gender: String = "",
    val goal: String = "",
    val avatarUri: String? = null, // картинка на телефоні
    val isDarkTheme: Boolean = false
)