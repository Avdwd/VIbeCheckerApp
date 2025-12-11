package com.example.vibechecker.ui.diary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibechecker.data.local.entity.MoodEntity
import com.example.vibechecker.data.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntryDetailViewModel @Inject constructor(
    private val repository: MoodRepository,
    savedStateHandle: SavedStateHandle // Ця штука вміє читати аргументи навігації
) : ViewModel() {

    // Стан: тут буде зберігатися наш знайдений запис
    private val _entry = MutableStateFlow<MoodEntity?>(null)
    val entry: StateFlow<MoodEntity?> = _entry.asStateFlow()

    // Стан: тут буде порада від "ШІ"
    private val _aiAdvice = MutableStateFlow("")
    val aiAdvice: StateFlow<String> = _aiAdvice.asStateFlow()

    init {
        // 1. Отримуємо ID, який ми передали через NavGraph ("entryId")
        val entryId: Int? = savedStateHandle["entryId"]

        if (entryId != null) {
            loadEntry(entryId)
        }
    }

    private fun loadEntry(id: Int) {
        viewModelScope.launch {
            // 2. Завантажуємо запис з БД
            val loadedEntry = repository.getMoodById(id)
            _entry.value = loadedEntry

            // 3. Генеруємо пораду на основі настрою
            loadedEntry?.let {
                _aiAdvice.value = generateFakeAIAdvice(it.moodValue)
            }
        }
    }

    // Проста логіка генерації поради (заглушка для ШІ)
    private fun generateFakeAIAdvice(mood: Int): String {
        return when (mood) {
            1 -> "Схоже, день був важким. Не забувайте про відпочинок і сон. Завтра буде новий день!"
            2 -> "Спробуйте проаналізувати, що саме вас засмутило. Коротка прогулянка може допомогти."
            3 -> "Рівний день — це стабільність. Спробуйте додати трохи яскравих емоцій ввечері."
            4 -> "Чудовий настрій! Використайте цю енергію для творчості або спілкування з близькими."
            5 -> "Ви сяєте! Запам'ятайте цей момент і поділіться позитивом з оточуючими."
            else -> "Слухайте своє серце."
        }
    }
}