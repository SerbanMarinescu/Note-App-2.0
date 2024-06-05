package com.example.newnotesapp.presentation.notes_screen

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newnotesapp.presentation.navigation.NoteDetailScreenRoute
import com.example.newnotesapp.presentation.note_details_screen.components.TransparentTextField
import com.example.newnotesapp.presentation.notes_screen.components.DefaultRadioButton
import com.example.newnotesapp.presentation.notes_screen.components.NoteItem
import com.example.newnotesapp.presentation.notes_screen.util.OrderType
import com.example.newnotesapp.presentation.util.BiometricAuthenticatorActivity
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotesScreen(
    state: NoteState,
    onEvent: (NoteEvent) -> Unit,
    navigateTo: (NoteDetailScreenRoute) -> Unit
) {

    val snackBarState = remember{
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val enrollLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK && activityResult.data?.getBooleanExtra("AUTH_SUCCESS", false) == true) {

            val noteId = activityResult.data?.getStringExtra("NoteId") ?: return@rememberLauncherForActivityResult
            val noteColor = activityResult.data?.getIntExtra("NoteColor", 0) ?: return@rememberLauncherForActivityResult

            navigateTo(
                NoteDetailScreenRoute(
                    noteId = noteId,
                    noteColor = noteColor
                )
            )
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigateTo(NoteDetailScreenRoute())
                },
                containerColor = Color.Black,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add new note"
                )
            }
        },
        snackbarHost = { SnackbarHost(snackBarState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Notes",
                    fontWeight = FontWeight.Bold,
                    fontSize = 35.sp,
                    color = Color.White
                )
                IconButton(
                    onClick = {
                        onEvent(NoteEvent.ChangeMenuVisibility)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        modifier = Modifier.size(50.dp),
                        tint = Color.White
                    )
                }
            }
            AnimatedVisibility(visible = state.isMenuVisible) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    DefaultRadioButton(
                        text = "Order By Date",
                        isSelected = state.orderType == OrderType.TIMESTAMP,
                        onClick = {
                            onEvent(NoteEvent.OrderNotes(OrderType.TIMESTAMP))
                        }
                    )
                    DefaultRadioButton(
                        text = "Group By Color",
                        isSelected = state.orderType == OrderType.COLOR,
                        onClick = {
                            onEvent(NoteEvent.OrderNotes(OrderType.COLOR))
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Note",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                        TransparentTextField(
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            text = state.searchFieldText,
                            hint = "Search by Title...",
                            hintColor = Color.White,
                            isHintVisible = state.isSearchHintVisible,
                            onValueChange = {
                                onEvent(NoteEvent.SearchNote(it))
                            },
                            onFocusChange = {
                                onEvent(NoteEvent.ChangeSearchFieldFocus(it))
                            },
                            textStyle = TextStyle(color = Color.White, fontSize = 20.sp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(35.dp))
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    state.notes,
                    key = { note ->
                        note.id!!
                    }
                ) { note ->
                    NoteItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (note.isLocked) {
                                    val intent = Intent(
                                        context,
                                        BiometricAuthenticatorActivity::class.java
                                    ).apply {
                                        putExtra("NoteId", note.id.toString())
                                        putExtra("NoteColor", note.color)
                                    }
                                    enrollLauncher.launch(intent)
                                } else {
                                    navigateTo(
                                        NoteDetailScreenRoute(
                                            noteId = note.id.toString(),
                                            noteColor = note.color
                                        )
                                    )
                                }
                            },
                        note = note,
                        timeStamp = note.timestamp,
                        isNoteLocked = note.isLocked,
                        onDeleteClick = {
                            onEvent(NoteEvent.DeleteNote(note))

                            scope.launch {
                                val result = snackBarState.showSnackbar(
                                    message = "Note Deleted",
                                    actionLabel = "Undo",
                                    duration = SnackbarDuration.Long
                                )
                                if(result == SnackbarResult.ActionPerformed){
                                    onEvent(NoteEvent.RestoreDeletedNote)
                                }
                            }
                        },
                        getNoteContent = {
                            RichTextState().setHtml(note.content)
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}