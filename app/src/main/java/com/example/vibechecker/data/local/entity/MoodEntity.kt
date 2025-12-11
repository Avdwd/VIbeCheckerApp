package com.example.vibechecker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// @Entity каже Room, що це таблиця в базі даних
@Entity(tableName = "mood_table")
data class MoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Унікальний номер запису (створюється сам)

    val moodValue: Int, // Настрій від 1 до 5
    val note: String? = null, // Текст опису дня
    val timestamp: Long = System.currentTimeMillis() // Час створення (в мілісекундах)
) {
    // Допоміжна функція: перетворює час у красиву дату "12/05/2024"
    fun getFormattedDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun getDayOfWeek(): String {
        // "EEE" дає скорочений день тижня (Пн, Вт, Ср...)
        val sdf = SimpleDateFormat("EEE", Locale("uk", "UA"))
        return sdf.format(Date(timestamp)).replaceFirstChar { it.uppercase() }
    }
}