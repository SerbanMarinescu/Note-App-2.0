package com.example.newnotesapp.presentation.note_details_screen.util

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun showSnackBarWithCustomDuration(
    scope: CoroutineScope,
    snackBarState: SnackbarHostState,
    message: String,
    duration: Long
) {
    val snackBarJob = scope.launch {
        snackBarState.showSnackbar(
            message = message,
            duration = SnackbarDuration.Long
        )
    }
    delay(duration)
    snackBarJob.cancel()
}