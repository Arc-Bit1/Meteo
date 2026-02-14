package com.example.meteo.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WeatherAnimation(icon: String) {
    val transition = rememberInfiniteTransition(label = "weather")
    val x = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "x"
    )

    Canvas(modifier = Modifier.fillMaxWidth().height(120.dp)) {
        val code = icon.toIntOrNull() ?: 1
        val isRainy = code in 8..12
        if (isRainy) {
            repeat(20) { i ->
                drawLine(
                    color = Color(0xFF5DADE2),
                    start = Offset(i * size.width / 20f + x.value * 20f, 20f),
                    end = Offset(i * size.width / 20f + x.value * 20f, size.height),
                    strokeWidth = 3f
                )
            }
        } else {
            drawCircle(
                color = Color(0xFFFFD54F),
                radius = 35f,
                center = Offset(size.width * (0.2f + x.value * 0.2f), size.height * 0.35f)
            )
        }
    }
}
