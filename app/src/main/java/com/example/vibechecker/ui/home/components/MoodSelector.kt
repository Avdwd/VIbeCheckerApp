package com.example.vibechecker.ui.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.vibechecker.R

@Composable
fun MoodSelector(
    selectedMood: Int, // 1..5
    onMoodSelected: (Int) -> Unit
) {

    val moods = listOf(
        1 to R.drawable.ic_mood_1,
        2 to R.drawable.ic_mood_2,
        3 to R.drawable.ic_mood_3,
        4 to R.drawable.ic_mood_4,
        5 to R.drawable.ic_mood_5
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        moods.forEach { (level, iconRes) ->
            MoodIcon(
                iconRes = iconRes,
                isSelected = level == selectedMood,
                onClick = { onMoodSelected(level) }
            )
        }
    }
}

@Composable
fun MoodIcon(
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

    val size = if (isSelected) 48.dp else 40.dp

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(size)
        )
    }
}