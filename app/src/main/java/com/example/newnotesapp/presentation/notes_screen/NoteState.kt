package com.example.newnotesapp.presentation.notes_screen

import com.example.newnotesapp.data.local.Note
import com.example.newnotesapp.presentation.notes_screen.util.OrderType

data class NoteState(
    val notes: List<Note> = emptyList(),
    val orderType: OrderType = OrderType.TIMESTAMP,
    val isMenuVisible: Boolean = false,
    val searchFieldText: String = "",
    val isSearchHintVisible: Boolean = true
)
