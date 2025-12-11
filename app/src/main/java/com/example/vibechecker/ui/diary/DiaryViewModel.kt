package com.example.vibechecker.ui.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibechecker.data.local.entity.MoodEntity
import com.example.vibechecker.data.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val repository: MoodRepository
) : ViewModel() {

    // Живий список записів. Як тільки щось змінюється в БД, цей список оновлюється.
    val moodEntries: StateFlow<List<MoodEntity>> = repository.allMoods
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Функція видалення
    fun deleteEntry(entry: MoodEntity) {
        viewModelScope.launch {
            repository.deleteMood(entry)
        }
    }
}