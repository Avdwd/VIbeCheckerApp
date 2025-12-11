package com.example.vibechecker.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest // <--- Імпорт
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    // Оновлений метод реєстрації: тепер приймає name
    suspend fun register(email: String, pass: String, name: String): Result<FirebaseUser?> {
        return try {
            // 1. Створюємо акаунт
            val result = firebaseAuth.createUserWithEmailAndPassword(email, pass).await()
            val user = result.user

            // 2. Якщо успішно - оновлюємо ім'я профілю в Firebase
            if (user != null) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name) // Встановлюємо ім'я
                    .build()

                user.updateProfile(profileUpdates).await()
                // Оновлюємо об'єкт user (щоб він підтягнув нове ім'я)
                user.reload().await()
            }

            Result.success(firebaseAuth.currentUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //Підтягуємо ім'я з Firebase
    suspend fun login(email: String, pass: String): Result<FirebaseUser?> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, pass).await()
            Result.success(result.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Dидалення акаунту
    suspend fun deleteAccount(): Result<Unit> {
        return try {
            // Видаляємо поточного юзера з Firebase
            firebaseAuth.currentUser?.delete()?.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}