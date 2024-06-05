package com.example.newnotesapp.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.newnotesapp.presentation.note_details_screen.NoteDetailsEvent
import com.example.newnotesapp.presentation.note_details_screen.NoteDetailsScreen
import com.example.newnotesapp.presentation.note_details_screen.NoteDetailsViewModel
import com.example.newnotesapp.presentation.notes_screen.NotesScreen
import com.example.newnotesapp.presentation.notes_screen.NotesViewModel
import com.example.newnotesapp.presentation.util.BiometricManager
import com.mohamedrejeb.richeditor.model.rememberRichTextState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NotesScreenRoute) {
        composable<NotesScreenRoute> {
            val viewModel = hiltViewModel<NotesViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            NotesScreen(
                state = state,
                onEvent = viewModel::onEvent,
                navigateTo = {
                    navController.navigate(it)
                }
            )
        }
        composable<NoteDetailScreenRoute> { entry ->
            val (noteId, noteColor) = entry.toRoute<NoteDetailScreenRoute>()

            val viewModel = hiltViewModel<NoteDetailsViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = noteId) {
                viewModel.onEvent(NoteDetailsEvent.InitializeNoteById(noteId?.toInt()))
            }

            viewModel.richTextState = rememberRichTextState()

            NoteDetailsScreen(
                state = state,
                noteResultFlow = viewModel.noteResult,
                onEvent = viewModel::onEvent,
                noteColor = noteColor,
                richTextState = viewModel.richTextState,
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}