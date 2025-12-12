package com.example.vibechecker.ui.diary


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vibechecker.R

@Composable
fun DiaryItem(
    moodIcon: Int, // ID ресурсу іконки
    date: String,  // текст дати
    moodNote: String? = null, // Опис
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), // Відступ між картками
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface, // Колір картки
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Ліва частина
            Icon(
                painter = painterResource(id = moodIcon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary, // Фарбуємо в основний колір
                modifier = Modifier.size(40.dp)
            )


            Text(
                text = date,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Preview
@Composable
fun DiaryItemPreview() {
    DiaryItem(
        moodIcon = R.drawable.ic_mood_5,
        date = "01/05/2024",
        onClick = {}
    )
}