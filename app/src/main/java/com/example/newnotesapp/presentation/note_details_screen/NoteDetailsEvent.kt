package com.example.newnotesapp.presentation.note_details_screen

import androidx.compose.ui.focus.FocusState
import com.example.newnotesapp.presentation.note_details_screen.util.TextModificationEvent

sealed interface NoteDetailsEvent {
    data class InitializeNoteById(val noteId: Int?): NoteDetailsEvent
    data class ChangedTitle(val title: String): NoteDetailsEvent
    data class ChangedColor(val color: Int): NoteDetailsEvent
    data class ChangeTitleFocus(val focusState: FocusState): NoteDetailsEvent
    data class ChangeContentFocus(val focusState: FocusState): NoteDetailsEvent
    data object SaveNote: NoteDetailsEvent
    data object LockNote: NoteDetailsEvent
    data object ToggleColorPicker: NoteDetailsEvent
    data object ToggleFontSizeDisplay: NoteDetailsEvent
    data class ToggleSelectedOption(val textEvent: TextModificationEvent, val index: Int): NoteDetailsEvent
}
