package com.example.newnotesapp.presentation.note_details_screen.util

sealed interface NoteResult {
    data class Error(val message: String): NoteResult
    data object Success: NoteResult
}