package com.example.newnotesapp.presentation.notes_screen

import androidx.compose.ui.focus.FocusState
import com.example.newnotesapp.data.local.Note
import com.example.newnotesapp.presentation.notes_screen.util.OrderType

sealed interface NoteEvent {
    data class DeleteNote(val note: Note): NoteEvent
    data object RestoreDeletedNote: NoteEvent
    data class OrderNotes(val orderType: OrderType): NoteEvent
    data object ChangeMenuVisibility: NoteEvent
    data class SearchNote(val query: String): NoteEvent
    data class ChangeSearchFieldFocus(val focusState: FocusState): NoteEvent
}