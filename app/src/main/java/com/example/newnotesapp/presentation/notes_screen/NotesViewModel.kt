package com.example.newnotesapp.presentation.notes_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newnotesapp.data.local.Note
import com.example.newnotesapp.data.local.NoteDatabase
import com.example.newnotesapp.presentation.notes_screen.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val db: NoteDatabase
): ViewModel() {

    private val _state = MutableStateFlow(NoteState())
    val state = _state.asStateFlow()

    private var recentlyDeletedNote: Note? = null
    private var getNotesJob: Job? = null

    init {
        getNotes(OrderType.TIMESTAMP)
    }

    fun onEvent(event: NoteEvent) {
        when(event) {
            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    recentlyDeletedNote = event.note
                    db.noteDao.deleteNote(event.note)
                }
            }

            NoteEvent.RestoreDeletedNote -> {
                viewModelScope.launch {
                    db.noteDao.upsertNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }

            is NoteEvent.OrderNotes -> {
                _state.update {
                    it.copy(orderType = event.orderType)
                }
                getNotes(event.orderType)
            }

            NoteEvent.ChangeMenuVisibility -> {
                _state.update {
                    it.copy(
                        isMenuVisible = !state.value.isMenuVisible,
                        searchFieldText = "",
                        isSearchHintVisible = true
                    )
                }
                getNotes(state.value.orderType)
            }

            is NoteEvent.SearchNote -> {
                _state.update {
                    it.copy(searchFieldText = event.query)
                }
                getNotesBySearchQuery(event.query)
            }

            is NoteEvent.ChangeSearchFieldFocus -> {
                _state.update {
                    it.copy(isSearchHintVisible = !event.focusState.isFocused && state.value.searchFieldText.isBlank())
                }
            }
        }
    }

    private fun getNotesByOrderType(orderType: OrderType): Flow<List<Note>> {
        return when(orderType) {
            OrderType.COLOR -> {
                db.noteDao.getAllNotesGroupedByColor()
            }
            OrderType.TIMESTAMP -> {
                db.noteDao.getAllNotes()
            }
        }
    }

    private fun getNotes(orderType: OrderType) {
        getNotesJob?.cancel()
        getNotesJob = getNotesByOrderType(orderType).onEach { noteList ->
            _state.update {
                it.copy(notes = noteList)
            }
        }.launchIn(viewModelScope)
    }

    private fun getNotesBySearchQuery(query: String) {
        getNotesJob?.cancel()
        getNotesJob = viewModelScope.launch {
            db.noteDao.searchNotesByTitle(query).collect { notes ->
                _state.update {
                    it.copy(notes = notes)
                }
            }
        }
    }
}

