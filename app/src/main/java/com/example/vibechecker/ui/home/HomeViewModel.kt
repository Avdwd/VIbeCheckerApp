package com.example.vibechecker.ui.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibechecker.data.local.dao.UserDao
import com.example.vibechecker.data.local.entity.MoodEntity
import com.example.vibechecker.data.local.entity.UserEntity
import com.example.vibechecker.data.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: MoodRepository,
    userDao: UserDao
) : ViewModel() {

    //останні 7 записів
    val last7Entries: StateFlow<List<MoodEntity>> = repository.last7Moods
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    //дані для графіку
    val moodValues: StateFlow<List<Int>> = last7Entries
        .map { entries: List<MoodEntity> -> // вказуємо тип
            entries.map { it.moodValue }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // підписи днів для графіку
    val dayLabels: StateFlow<List<String>> = last7Entries
        .map { entries: List<MoodEntity> ->
            entries.map { it.getDayOfWeek() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val userProfile: StateFlow<UserEntity?> = userDao.getUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}


