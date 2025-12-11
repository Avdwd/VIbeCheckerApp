package com.example.vibechecker.ui.auth

import android.R.attr.name
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibechecker.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.vibechecker.data.local.dao.UserDao // <--- Додали
import com.example.vibechecker.data.local.entity.UserEntity

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val userDao: UserDao
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _registerSuccess = MutableStateFlow(false)
    val registerSuccess: StateFlow<Boolean> = _registerSuccess

    fun register(name: String, email: String, pass: String, confirmPass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _error.value = "Заповніть всі поля"
            return
        }
        if (pass != confirmPass) {
            _error.value = "Паролі не співпадають"
            return
        }
        if (pass.length < 6) {
            _error.value = "Пароль має бути не менше 6 символів"
            return
        }

        if (name.isBlank()) {
            _error.value = "Введіть ім'я"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            // 1. Реєструємо в Firebase з іменем
            val result = repository.register(email, pass, name)

            if (result.isSuccess) {
                // 2. УСПІХ! Зберігаємо ім'я в локальну базу Room
                val newUser = UserEntity(
                    id = 1, // Завжди 1 для поточного юзера
                    name = name,
                    // Інші поля поки пусті
                )
                userDao.insertUser(newUser)

                _registerSuccess.value = true
            } else {
                // ... обробка помилок ...
            }
            _isLoading.value = false
        }
    }
}