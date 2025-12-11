package com.example.vibechecker.di

import android.app.Application
import androidx.room.Room
import com.example.vibechecker.data.local.VibeDatabase

import com.example.vibechecker.data.local.dao.MoodDao
import com.example.vibechecker.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.google.firebase.auth.FirebaseAuth

@Module
@InstallIn(SingletonComponent::class) // Цей модуль живе весь час роботи програми
object AppModule {

    // 1. Створюємо файл бази даних
    @Provides
    @Singleton
    fun provideVibeDatabase(app: Application): VibeDatabase {
        return Room.databaseBuilder(
            app,
            VibeDatabase::class.java,
            "vibe_checker_db" // Ім'я файлу на телефоні
        ).fallbackToDestructiveMigration() // Дозволяє перестворити БД при зміні версії
            .build()
    }

    // 2. Створюємо DAO (щоб Hilt міг його видавати іншим класам)
    @Provides
    @Singleton
    fun provideMoodDao(db: VibeDatabase): MoodDao {
        return db.moodDao()
    }


    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideUserDao(db: VibeDatabase): UserDao {
        return db.userDao()
    }
}