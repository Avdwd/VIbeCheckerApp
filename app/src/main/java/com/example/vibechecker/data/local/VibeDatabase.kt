package com.example.vibechecker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vibechecker.data.local.dao.MoodDao
import com.example.vibechecker.data.local.dao.UserDao
import com.example.vibechecker.data.local.entity.MoodEntity
import com.example.vibechecker.data.local.entity.UserEntity

@Database(
    entities = [MoodEntity::class, UserEntity::class], // <--- ДОДАЛИ UserEntity
    version = 2, // <--- ЗБІЛЬШИЛИ ВЕРСІЮ (важливо!)
    exportSchema = false
)
abstract class VibeDatabase : RoomDatabase() {
    abstract fun moodDao(): MoodDao
    abstract fun userDao(): UserDao // <--- ДОДАЛИ

}