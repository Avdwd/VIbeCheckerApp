package com.example.vibechecker.di

import android.app.Application
import androidx.room.Room
import com.example.vibechecker.data.local.VibeDatabase

import com.example.vibechecker.data.local.dao.MoodDao
import com.example.vibechecker.data.local.dao.UserDao
import com.google.ai.client.generativeai.GenerativeModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.google.firebase.auth.FirebaseAuth
import com.example.vibechecker.BuildConfig

@Module
@InstallIn(SingletonComponent::class) // модуль живе весь час роботи програми
object AppModule {

    //Створюємо файл бази даних
    @Provides
    @Singleton
    fun provideVibeDatabase(app: Application): VibeDatabase {
        return Room.databaseBuilder(
            app,
            VibeDatabase::class.java,
            "vibe_checker_db"
        ).fallbackToDestructiveMigration() // Дозволяє перестворити БД при зміні версії
            .build()
    }

    //Створюємо DAO щоб Hilt міг його видавати іншим класам
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

    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel {
        return GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = BuildConfig.GEMINI_API_KEY
        )
    }
}