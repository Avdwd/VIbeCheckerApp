package com.example.vibechecker.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibechecker.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.vibechecker.data.local.dao.UserDao
import com.example.vibechecker.data.local.entity.UserEntity

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val userDao: UserDao
) : ViewModel() {

    // Стани для UI
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Успішний вхід (сигнал для навігації)
    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _error.value = "Заповніть всі поля"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = repository.login(email, pass)

            if (result.isSuccess) {
                val firebaseUser = result.getOrNull()

                val cloudName = firebaseUser?.displayName ?: "User"

                val localUser = UserEntity(
                    id = 1,
                    name = cloudName

                )
                userDao.insertUser(localUser)

                _loginSuccess.value = true
            } else {
                _error.value = result.exceptionOrNull()?.localizedMessage ?: "Помилка входу"
            }
            _isLoading.value = false
        }
    }
}