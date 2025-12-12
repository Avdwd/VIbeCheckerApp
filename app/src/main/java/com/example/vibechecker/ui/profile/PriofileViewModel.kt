package com.example.vibechecker.ui.profile


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibechecker.data.local.VibeDatabase
import com.example.vibechecker.data.local.dao.UserDao
import com.example.vibechecker.data.local.entity.UserEntity
import com.example.vibechecker.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userDao: UserDao,
    private val authRepository: AuthRepository,
    private val database: VibeDatabase
) : ViewModel() {

    val user: StateFlow<UserEntity?> = userDao.getUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun saveProfile(name: String, age: String, gender: String, goal: String, isDarkTheme: Boolean, avatarUri: String?) {
        viewModelScope.launch {
            val updatedUser = UserEntity(
                id = 1,
                name = name,
                age = age,
                gender = gender,
                goal = goal,
                isDarkTheme = isDarkTheme,
                avatarUri = avatarUri
            )
            userDao.insertUser(updatedUser)
        }
    }

    //ВИХІД
    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.logout()
            database.clearAllTables() // Очищаємо всі записи (щоденник, профіль) з телефону

            launch(Dispatchers.Main) { onSuccess() }
        }
    }

    // ВИДАЛЕННЯ
    fun deleteAccount(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = authRepository.deleteAccount()
            if (result.isSuccess) {
                // видалили з хмари + чистимо телефон
                launch(Dispatchers.IO) {
                    database.clearAllTables()
                }
                onSuccess()
            } else {
                onError(result.exceptionOrNull()?.localizedMessage ?: "Помилка видалення")
            }
        }
    }
}