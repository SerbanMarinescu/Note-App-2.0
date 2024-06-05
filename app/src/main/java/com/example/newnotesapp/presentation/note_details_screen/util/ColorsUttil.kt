package com.example.newnotesapp.presentation.note_details_screen.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

fun getColorAtPosition(position: Offset, width: Float, gradientColors: List<Color>): Color {
    val proportion = position.x / width
    val colorIndex = (proportion * (gradientColors.size - 1)).toInt()
    return gradientColors[colorIndex]
}