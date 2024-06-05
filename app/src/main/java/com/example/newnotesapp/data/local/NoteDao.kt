package com.example.newnotesapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Upsert
    suspend fun upsertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM Note WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Query("SELECT * FROM Note ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM Note ORDER BY color, timestamp DESC")
    fun getAllNotesGroupedByColor(): Flow<List<Note>>

    @Query("SELECT * FROM NOTE WHERE title LIKE '%' || :searchQuery || '%'")
    fun searchNotesByTitle(searchQuery: String): Flow<List<Note>>
}