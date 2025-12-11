package com.example.vibechecker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vibechecker.data.local.entity.MoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {

    // Зберегти запис (якщо такий ID вже є - замінити його)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMood(mood: MoodEntity)

    // Видалити запис
    @Delete
    suspend fun deleteMood(mood: MoodEntity)

    // Отримати ВСІ записи (від нових до старих)
    // Flow означає "живий потік": якщо щось зміниться в БД, список оновиться сам
    @Query("SELECT * FROM mood_table ORDER BY timestamp DESC")
    fun getAllMoods(): Flow<List<MoodEntity>>

    // Отримати останні 7 записів (для графіку)
    @Query("SELECT * FROM mood_table ORDER BY timestamp ASC LIMIT 7")
    fun getLast7Moods(): Flow<List<MoodEntity>>

    @Query("SELECT * FROM mood_table WHERE id = :id")
    suspend fun getMoodById(id: Int): MoodEntity?
}