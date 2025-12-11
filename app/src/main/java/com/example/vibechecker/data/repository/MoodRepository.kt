package com.example.vibechecker.data.repository

import com.example.vibechecker.data.local.dao.MoodDao
import com.example.vibechecker.data.local.entity.MoodEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject



class MoodRepository @Inject constructor(
    private val moodDao: MoodDao
) {
    // 1. Отримати всі записи (для списку щоденника)
    val allMoods: Flow<List<MoodEntity>> = moodDao.getAllMoods()

    // 2. Отримати останні 7 записів (для графіка на головній)
    val last7Moods: Flow<List<MoodEntity>> = moodDao.getLast7Moods()

    // 3. Зберегти запис
    suspend fun saveMood(moodValue: Int, note: String) {
        val entry = MoodEntity(
            moodValue = moodValue,
            note = note,
            timestamp = System.currentTimeMillis()
        )
        moodDao.insertMood(entry)
    }

    // 4. Видалити запис
    suspend fun deleteMood(mood: MoodEntity) {
        moodDao.deleteMood(mood)
    }

    // 5. Отримати конкретний запис
    suspend fun getMoodById(id: Int): MoodEntity? {
        return moodDao.getMoodById(id)
    }
}