package com.example.newnotesapp.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object NotesScreenRoute

@Serializable
data class NoteDetailScreenRoute(
    val noteId: String? = null,
    val noteColor: Int = 0
)