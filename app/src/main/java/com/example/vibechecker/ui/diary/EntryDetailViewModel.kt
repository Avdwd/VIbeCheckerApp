package com.example.vibechecker.ui.diary


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibechecker.data.local.entity.MoodEntity
import com.example.vibechecker.data.repository.MoodRepository
import com.google.ai.client.generativeai.GenerativeModel // <--- Імпорт
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntryDetailViewModel @Inject constructor(
    private val repository: MoodRepository,
    savedStateHandle: SavedStateHandle,
    private val generativeModel: GenerativeModel
) : ViewModel() {

    private val _entry = MutableStateFlow<MoodEntity?>(null)
    val entry: StateFlow<MoodEntity?> = _entry.asStateFlow()

    private val _aiAdvice = MutableStateFlow("Аналізую твій стан...") // Початковий текст
    val aiAdvice: StateFlow<String> = _aiAdvice.asStateFlow()

    init {
        val entryId: Int? = savedStateHandle["entryId"]
        if (entryId != null) {
            loadEntry(entryId)
        }
    }

    private fun loadEntry(id: Int) {
        viewModelScope.launch {
            val loadedEntry = repository.getMoodById(id)
            _entry.value = loadedEntry

            // Як тільки завантажили запис — просимо ШІ дати пораду
            loadedEntry?.let {
                generateRealAIAdvice(it)
            }
        }
    }

    private fun generateRealAIAdvice(entry: MoodEntity) {
        viewModelScope.launch {
            try {
                //Формуємо Промпт
                // Ми пояснюємо ШІ, хто він і що треба робити
                val prompt = """
                    Ти - досвідчений, емпатичний психолог. 
                    Користувач оцінив свій настрій на ${entry.moodValue} з 5.
                    Його нотатка про день: "${entry.note ?: "без опису"}".
                    
                    Дай коротку (максимум 2-3 речення), теплу та корисну пораду українською мовою. 
                    Звертайся до користувача на "ти". Підтримай його або порадій за нього.
                """.trimIndent()

                //Відправляємо запит
                val response = generativeModel.generateContent(prompt)

                //Показуємо результат
                _aiAdvice.value = response.text ?: "Не вдалося отримати пораду."

            } catch (e: Exception) {
                _aiAdvice.value = "ШІ зараз відпочиває (помилка з'єднання)."
                e.printStackTrace()
            }
        }
    }
}