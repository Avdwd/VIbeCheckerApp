package com.example.vibechecker.ui.home.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.vibechecker.R
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.DefaultPointConnector
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf

@Composable
fun MoodChart(
    moods: List<Int>,
    days: List<String>
) {

    val chartEntryModel = remember(moods) {
        val entries = moods.mapIndexed { index, moodValue ->
            FloatEntry(x = index.toFloat(), y = moodValue.toFloat())
        }
        entryModelOf(entries)
    }

    val horizontalAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->

        days.getOrElse(value.toInt()) { "" }
    }

    val lineColor = MaterialTheme.colorScheme.primary
    val gradientStart = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    val gradientEnd = MaterialTheme.colorScheme.primary.copy(alpha = 0.0f)


    val moodIcons = listOf(
        R.drawable.ic_mood_5,
        R.drawable.ic_mood_4,
        R.drawable.ic_mood_3,
        R.drawable.ic_mood_2,
        R.drawable.ic_mood_1
    )


    Row(
        modifier = Modifier
            .fillMaxSize()

    ) {


        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(bottom = 28.dp, top = 4.dp)
                .padding(end = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            moodIcons.forEach { iconRes ->
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,

                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Chart(
            chart = lineChart(
                lines = listOf(
                    LineChart.LineSpec(
                        lineColor = lineColor.toArgb(),
                        lineThicknessDp = 3f,

                        pointConnector = DefaultPointConnector(cubicStrength = 0.4f),

                        lineBackgroundShader = DynamicShaders.fromBrush(
                            brush = Brush.verticalGradient(
                                colors = listOf(gradientStart, gradientEnd)
                            )
                        )
                    )
                ),

            ),
            model = chartEntryModel,

            bottomAxis = rememberBottomAxis(
                valueFormatter = horizontalAxisValueFormatter,
                guideline = null
            ),

            modifier = Modifier.fillMaxSize()
        )
    }
}