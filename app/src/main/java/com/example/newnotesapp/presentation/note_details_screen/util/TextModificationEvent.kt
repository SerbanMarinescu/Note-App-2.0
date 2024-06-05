package com.example.newnotesapp.presentation.note_details_screen.util

import androidx.compose.ui.graphics.Color

sealed interface TextModificationEvent {
    data object Bold : TextModificationEvent
    data object Italic : TextModificationEvent
    data object Underline : TextModificationEvent
    data object ReduceFont : TextModificationEvent
    data object IncreaseFont : TextModificationEvent
    data class ColorText(val color: Color) : TextModificationEvent
    data class Align(val alignType: AlignType) : TextModificationEvent
    data object List : TextModificationEvent
}