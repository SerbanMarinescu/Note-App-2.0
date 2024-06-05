package com.example.newnotesapp.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.FormatAlignRight
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TextDecrease
import androidx.compose.material.icons.filled.TextIncrease
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.newnotesapp.presentation.note_details_screen.util.TextModification
import com.example.newnotesapp.ui.theme.Blue
import com.example.newnotesapp.ui.theme.Green
import com.example.newnotesapp.ui.theme.Orange
import com.example.newnotesapp.ui.theme.Purple
import com.example.newnotesapp.ui.theme.RedOrange
import com.example.newnotesapp.ui.theme.RedPink
import com.example.newnotesapp.ui.theme.Yellow

object Constants {
    const val DATABASE_NAME = "Notes.db"
    val NOTE_COLORS = listOf(Orange, Yellow, Blue, RedPink ,Green , RedOrange, Purple)
    val TEXT_MODIFICATION_OPTIONS = listOf(
        TextModification(
            iconId = Icons.Default.FormatBold
        ),
        TextModification(
            iconId = Icons.Default.FormatItalic
        ),
        TextModification(
            iconId = Icons.Default.FormatUnderlined
        ),
        TextModification(
            iconId = Icons.Default.TextDecrease
        ),
        TextModification(
            iconId = Icons.Default.TextIncrease
        ),
        TextModification(
            iconId = Icons.Default.FormatColorText
        ),
        TextModification(
            iconId = Icons.AutoMirrored.Filled.FormatAlignLeft
        ),
        TextModification(
            iconId = Icons.Default.FormatAlignCenter
        ),
        TextModification(
            iconId = Icons.AutoMirrored.Filled.FormatAlignRight
        ),
        TextModification(
            iconId = Icons.AutoMirrored.Filled.FormatListBulleted
        ),
    )
}