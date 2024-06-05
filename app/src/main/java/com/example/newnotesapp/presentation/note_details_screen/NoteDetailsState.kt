package com.example.newnotesapp.presentation.note_details_screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.newnotesapp.common.Constants
import com.example.newnotesapp.presentation.note_details_screen.util.TextModification

data class NoteDetailsState(
    val title: String = "",
    val isTitleHintVisible: Boolean = true,
    val isContentHintVisible: Boolean = true,
    val color: Int = Constants.NOTE_COLORS.random().toArgb(),
    val noteId: Int? = null,
    val isNoteLocked: Boolean = false,
    val lockedIconInitialColor: Animatable<Color, AnimationVector4D>? = null,
    val textModOptions: List<TextModification> = emptyList(),
    val selectableTextOptions: Map<Int, Boolean> = emptyMap(),
    val currentFontSize: TextUnit = 15.sp,
    val isColorPickerDialogVisible: Boolean = false,
    val isFontSizeVisible: Boolean = false
)
