package com.example.vibechecker.ui.diary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibechecker.data.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEntryViewModel @Inject constructor(
    private val repository: MoodRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _selectedMood = MutableStateFlow(3)
    val selectedMood: StateFlow<Int> = _selectedMood.asStateFlow()

    init {
        // Перевіряємо, чи передали нам настрій через навігацію
        val initialMood = savedStateHandle.get<String>("mood")?.toIntOrNull()
        if (initialMood != null) {
            _selectedMood.value = initialMood
        }
    }
    fun updateMood(newMood: Int) {
        _selectedMood.value = newMood
    }

    fun saveMood(note: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            // Використовуємо поточний _selectedMood.value
            repository.saveMood(_selectedMood.value, note)
            onSuccess()
        }
    }
}