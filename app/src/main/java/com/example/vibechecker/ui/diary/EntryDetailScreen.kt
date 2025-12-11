package com.example.vibechecker.ui.diary
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vibechecker.R
import com.example.vibechecker.ui.theme.VIbeCheckerTheme
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun EntryDetailScreen(
    entryId: Int,
    onBackClick: () -> Unit,
    viewModel: EntryDetailViewModel = hiltViewModel() // Підключаємо ViewModel
) {
    // Читаємо дані зі станів
    val entry by viewModel.entry.collectAsState()
    val aiRecommendation by viewModel.aiAdvice.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Запис") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        // Перевіряємо, чи завантажився запис
        if (entry != null) {
            // Зберігаємо entry в локальну змінну, щоб не писати entry!!
            val currentEntry = entry!!

            // Вибираємо іконку під настрій
            val moodIcon = when(currentEntry.moodValue) {
                1 -> R.drawable.ic_mood_1
                2 -> R.drawable.ic_mood_2
                3 -> R.drawable.ic_mood_3
                4 -> R.drawable.ic_mood_4
                5 -> R.drawable.ic_mood_5
                else -> R.drawable.ic_mood_3
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                // 1. ВЕРХНЯ КАРТКА (Реальні дані)
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            painter = painterResource(id = moodIcon),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = currentEntry.getFormattedDate(), // Реальна дата
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 2. ОПИС (Реальний текст)
                Text(
                    text = "Опис",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 1.dp
                ) {
                    Text(
                        // Якщо тексту немає, пишемо заглушку
                        text = if (currentEntry.note.isNullOrBlank()) "Без опису" else currentEntry.note,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(24.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 3. РЕКОМЕНДАЦІЇ (Згенеровані ViewModel)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_nav_profile),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Рекомендації від ШІ",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = aiRecommendation, // Текст з ViewModel
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontStyle = FontStyle.Italic
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        FeedbackButtons(
                            onLikeClick = { },
                            onDislikeClick = { }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        } else {
            // Якщо запис ще вантажиться або не знайдено
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Завантаження...")
            }
        }
    }
}

@Composable
fun FeedbackButtons(
    onLikeClick: () -> Unit,
    onDislikeClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {

        IconButton(
            onClick = onDislikeClick,
            modifier = Modifier
                .background(Color(0xFFEF5350).copy(alpha = 0.15f), CircleShape)
                .size(44.dp)
        ) {
            Icon(

                painter = painterResource(id = R.drawable.ic_dislike),
                contentDescription = "Dislike",
                tint = Color(0xFFEF5350),
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))


        IconButton(
            onClick = onLikeClick,
            modifier = Modifier
                .background(Color(0xFF66BB6A).copy(alpha = 0.15f), CircleShape)
                .size(44.dp)
        ) {
            Icon(

                painter = painterResource(id = R.drawable.ic_like),
                contentDescription = "Like",
                tint = Color(0xFF66BB6A),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
fun DetailPreview() {
    VIbeCheckerTheme {
        EntryDetailScreen(1, {})
    }
}