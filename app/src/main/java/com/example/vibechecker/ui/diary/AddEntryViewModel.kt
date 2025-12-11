package com.example.vibechecker.ui.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibechecker.data.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEntryViewModel @Inject constructor(
    private val repository: MoodRepository
) : ViewModel() {

    fun saveMood(moodValue: Int, note: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.saveMood(moodValue, note)
            onSuccess() // Повідомляємо UI, що все готово (можна закривати екран)
        }
    }
}