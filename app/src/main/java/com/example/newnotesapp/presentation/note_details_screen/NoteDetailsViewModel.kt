package com.example.newnotesapp.presentation.note_details_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.Animatable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newnotesapp.common.Constants.TEXT_MODIFICATION_OPTIONS
import com.example.newnotesapp.data.local.Note
import com.example.newnotesapp.data.local.NoteDatabase
import com.example.newnotesapp.presentation.note_details_screen.util.AlignType
import com.example.newnotesapp.presentation.note_details_screen.util.NoteResult
import com.example.newnotesapp.presentation.note_details_screen.util.TextModification
import com.example.newnotesapp.presentation.note_details_screen.util.TextModificationEvent
import com.example.newnotesapp.presentation.util.toUtcTimestamp
import com.example.newnotesapp.ui.theme.GreenValid
import com.mohamedrejeb.richeditor.model.RichTextState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    private val db: NoteDatabase
): ViewModel() {

    private val _state = MutableStateFlow(NoteDetailsState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<NoteResult>()
    val noteResult = eventChannel.receiveAsFlow()

    var richTextState by mutableStateOf(RichTextState())

    private val textModificationOptions = mutableStateListOf<TextModification>()
    private val selectableTextOptions = mutableStateMapOf<Int, Boolean>()

    private var fontDisplayJob: Job? = null

    init {
        textModificationOptions.addAll(TEXT_MODIFICATION_OPTIONS)
        for(i in 0..< textModificationOptions.size) {
            selectableTextOptions[i] = false
        }

        _state.update {
            it.copy(
                textModOptions = textModificationOptions,
                selectableTextOptions = selectableTextOptions
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: NoteDetailsEvent) {
        when(event) {
            is NoteDetailsEvent.InitializeNoteById -> {
                event.noteId?.let { noteId ->
                    viewModelScope.launch {
                        val note = db.noteDao.getNoteById(noteId)
                        note?.let {
                            richTextState.setHtml(note.content)

                            _state.update {
                                it.copy(
                                    title = note.title,
                                    color = note.color,
                                    noteId = noteId,
                                    isContentHintVisible = false,
                                    isTitleHintVisible = false,
                                    isNoteLocked = note.isLocked,
                                    lockedIconInitialColor = if(note.isLocked) Animatable(Color.Red) else Animatable(GreenValid)
                                )
                            }
                        }
                    }
                }
            }

            is NoteDetailsEvent.ChangedColor -> {
                _state.update {
                    it.copy(color = event.color)
                }
            }
            is NoteDetailsEvent.ChangedTitle -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }
            }

            NoteDetailsEvent.SaveNote -> {
                saveNote()
            }

            is NoteDetailsEvent.ChangeContentFocus -> {
                _state.update {
                    it.copy(
                        isContentHintVisible = !event.focusState.isFocused && richTextState.annotatedString.isBlank()
                    )
                }
            }
            is NoteDetailsEvent.ChangeTitleFocus -> {
                _state.update {
                    it.copy(
                        isTitleHintVisible = !event.focusState.isFocused && it.title.isBlank()
                    )
                }
            }

            NoteDetailsEvent.LockNote -> {
                _state.update {
                    it.copy(isNoteLocked = !state.value.isNoteLocked)
                }
            }

            is NoteDetailsEvent.ToggleSelectedOption -> {
                when(event.textEvent) {
                    is TextModificationEvent.ColorText -> {
                        applyTextModification(event.textEvent)
                    }
                    is TextModificationEvent.Align -> {
                        val alignType = event.textEvent.alignType
                        val alignmentOptions = mapOf(
                            AlignType.START to 6,
                            AlignType.CENTER to 7,
                            AlignType.END to 8
                        )
                        alignmentOptions.forEach { (align, index) ->
                            selectableTextOptions[index] = (align == alignType && !selectableTextOptions[index]!!)
                        }
                        _state.update {
                            it.copy(selectableTextOptions = selectableTextOptions)
                        }
                        applyTextModification(event.textEvent)
                    }
                    TextModificationEvent.ReduceFont,
                    TextModificationEvent.IncreaseFont -> {
                        applyTextModification(event.textEvent)
                    }
                    else -> {
                        selectableTextOptions[event.index] = !selectableTextOptions[event.index]!!
                        _state.update {
                            it.copy(selectableTextOptions = selectableTextOptions)
                        }
                        applyTextModification(event.textEvent)
                    }
                }

//                if(event.textEvent is TextModificationEvent.ColorText) {
//                    applyTextModification(event.textEvent)
//                    return
//                }
//
//                if(event.textEvent is TextModificationEvent.Align) {
//                    when(event.textEvent.alignType) {
//                        AlignType.START -> {
//                            if(!selectableTextOptions[6]!!) {
//                                selectableTextOptions[6] = true
//                                selectableTextOptions[7] = false
//                                selectableTextOptions[8] = false
//                            } else {
//                                selectableTextOptions[6] = false
//                            }
//                        }
//                        AlignType.CENTER -> {
//                            if(!selectableTextOptions[7]!!) {
//                                selectableTextOptions[6] = false
//                                selectableTextOptions[7] = true
//                                selectableTextOptions[8] = false
//                            } else {
//                                selectableTextOptions[7] = false
//                            }
//                        }
//                        AlignType.END -> {
//                            if(!selectableTextOptions[8]!!) {
//                                selectableTextOptions[6] = false
//                                selectableTextOptions[7] = false
//                                selectableTextOptions[8] = true
//                            } else {
//                                selectableTextOptions[8] = false
//                            }
//                        }
//                    }
//                    _state.update {
//                        it.copy(selectableTextOptions = selectableTextOptions)
//                    }
//                    applyTextModification(event.textEvent)
//                    return
//                }
//
//                if( event.textEvent == TextModificationEvent.ReduceFont ||
//                    event.textEvent == TextModificationEvent.IncreaseFont
//                    ) {
//                    applyTextModification(event.textEvent)
//                    return
//                }
//
//
//                selectableTextOptions[event.index] = !selectableTextOptions[event.index]!!
//
//                _state.update {
//                    it.copy(selectableTextOptions = selectableTextOptions)
//                }
//                applyTextModification(event.textEvent)
            }

            NoteDetailsEvent.ToggleColorPicker -> {
                selectableTextOptions[5] = !selectableTextOptions[5]!!
                _state.update {
                    it.copy(
                        isColorPickerDialogVisible = !state.value.isColorPickerDialogVisible,
                        selectableTextOptions = selectableTextOptions
                    )
                }
            }

            NoteDetailsEvent.ToggleFontSizeDisplay -> {
                fontDisplayJob?.cancel()
                fontDisplayJob = viewModelScope.launch {
                    _state.update {
                        it.copy(isFontSizeVisible = true)
                    }
                    delay(1000)
                    _state.update {
                        it.copy(isFontSizeVisible = false)
                    }
                }
            }
        }
    }

    private fun applyTextModification(textEvent: TextModificationEvent) {
        when(textEvent) {
            TextModificationEvent.Bold -> {
                richTextState.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
            }
            TextModificationEvent.Italic -> {
                richTextState.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
            }
            TextModificationEvent.Underline -> {
                richTextState.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
            }
            TextModificationEvent.ReduceFont -> {
                if(state.value.currentFontSize.value > 5) {
                    _state.update {
                        it.copy(
                            currentFontSize = (state.value.currentFontSize.value - 5).sp
                        )
                    }
                    richTextState.toggleSpanStyle(SpanStyle(fontSize = state.value.currentFontSize))
                }
            }
            TextModificationEvent.IncreaseFont -> {
                if(state.value.currentFontSize.value < 100) {
                    _state.update {
                        it.copy(
                            currentFontSize = (state.value.currentFontSize.value + 5).sp
                        )
                    }
                    richTextState.toggleSpanStyle(SpanStyle(fontSize = state.value.currentFontSize))
                }
            }
            is TextModificationEvent.ColorText -> {
                richTextState.toggleSpanStyle(SpanStyle(color = textEvent.color))
            }
            is TextModificationEvent.Align -> {
                richTextState.toggleParagraphStyle(ParagraphStyle(
                    textAlign = when(textEvent.alignType) {
                        AlignType.START -> {
                            TextAlign.Start
                        }
                        AlignType.CENTER -> {
                            TextAlign.Center
                        }
                        AlignType.END -> {
                            TextAlign.End
                        }
                    }
                ))
            }
            TextModificationEvent.List -> {
                richTextState.toggleUnorderedList()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveNote() {
        viewModelScope.launch {

            if(state.value.title.isBlank()) {
                eventChannel.send(NoteResult.Error("Title cannot be empty!"))
                return@launch
            }
            if(richTextState.annotatedString.isBlank()) {
                eventChannel.send(NoteResult.Error("Content cannot be empty!"))
                return@launch
            }

            val content = richTextState.toHtml()

            db.noteDao.upsertNote(
                Note(
                    title = state.value.title,
                    content = content,
                    timestamp = LocalDateTime.now().toUtcTimestamp(),
                    color = state.value.color,
                    isLocked = state.value.isNoteLocked,
                    id = state.value.noteId
                )
            )
            eventChannel.send(NoteResult.Success)
        }
    }
}
