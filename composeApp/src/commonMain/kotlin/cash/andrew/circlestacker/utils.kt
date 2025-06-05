package cash.andrew.circlestacker

import androidx.compose.ui.graphics.Color

val circleColors = listOf(
    Color(0xFFDE8607),
    Color(0xFFDE07D4),
    Color(0xFF4BDE07),
    Color(0xFF0786DE),
    Color(0xFF5B8548),
)

fun randomColor() = circleColors.random()
