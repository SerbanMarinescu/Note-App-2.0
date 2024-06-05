package com.example.newnotesapp.presentation.note_details_screen

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newnotesapp.R
import com.example.newnotesapp.common.Constants
import com.example.newnotesapp.presentation.note_details_screen.components.TransparentRichTextField
import com.example.newnotesapp.presentation.note_details_screen.components.TransparentTextField
import com.example.newnotesapp.presentation.note_details_screen.util.AlignType
import com.example.newnotesapp.presentation.note_details_screen.util.NoteResult
import com.example.newnotesapp.presentation.note_details_screen.util.TextModificationEvent
import com.example.newnotesapp.presentation.note_details_screen.util.getColorAtPosition
import com.example.newnotesapp.presentation.note_details_screen.util.showSnackBarWithCustomDuration
import com.example.newnotesapp.ui.theme.Blue
import com.example.newnotesapp.ui.theme.GreenValid
import com.example.newnotesapp.ui.theme.Orange
import com.example.newnotesapp.ui.theme.Purple
import com.mohamedrejeb.richeditor.model.RichTextState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NoteDetailsScreen(
    state: NoteDetailsState,
    noteResultFlow: Flow<NoteResult>,
    onEvent: (NoteDetailsEvent) -> Unit,
    noteColor: Int,
    richTextState: RichTextState,
    navigateBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val snackBarState = remember{
        SnackbarHostState()
    }

    val noteBackgroundAnimatable = remember {
        Animatable(if(noteColor == 0) Color(state.color) else Color(noteColor))
    }

    val newNoteLockBtnColor = remember {
        Animatable(GreenValid)
    }


    var colorPickerCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    val gradientColors = remember {
        listOf(Color.Black, Color.Red, Orange, Color.Yellow, Color.Green, GreenValid, Color.Cyan, Blue, Color.Blue, Color.Magenta, Purple)
    }
    var canvasWidth by remember {
        mutableFloatStateOf(0f)
    }
    var color by remember {
        mutableStateOf(Color.Black)
    }


    LaunchedEffect(state.noteId) {
        noteResultFlow.collect { noteResult ->
            when(noteResult) {
                is NoteResult.Error -> {
                    snackBarState.showSnackbar(message = noteResult.message, duration = SnackbarDuration.Short)
                }
                NoteResult.Success -> {
                    navigateBack()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarState) },
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Constants.NOTE_COLORS.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(15.dp, CircleShape)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 3.dp,
                                color = if (state.color == color.toArgb()) Color.Black else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    noteBackgroundAnimatable.animateTo(
                                        targetValue = Color(color.toArgb()),
                                        animationSpec = tween(durationMillis = 500)
                                    )
                                }
                                onEvent(NoteDetailsEvent.ChangedColor(color.toArgb()))
                            }
                    )
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 7.dp)
                        .weight(0.8f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.textModOptions.forEachIndexed { index, option ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .size(50.dp)
                                .then(
                                    if (state.selectableTextOptions[index] == true) {
                                        Modifier.border(
                                            5.dp,
                                            Color.Black,
                                            RoundedCornerShape(10.dp)
                                        )
                                    } else {
                                        Modifier
                                    }
                                )
                                .background(
                                    if (state.selectableTextOptions[index] == true) {
                                        if (index == 5) {
                                            Color(0xFFF5F5DC)
                                        } else {
                                            Color.Black
                                        }
                                    } else {
                                        Color(0xFFF5F5DC)
                                    }
                                )
                                .clickable {
                                    val textModificationEvent = when (index) {
                                        0 -> {
                                            TextModificationEvent.Bold
                                        }

                                        1 -> {
                                            TextModificationEvent.Italic
                                        }

                                        2 -> {
                                            TextModificationEvent.Underline
                                        }

                                        3 -> {
                                            onEvent(NoteDetailsEvent.ToggleFontSizeDisplay)
                                            TextModificationEvent.ReduceFont
                                        }

                                        4 -> {
                                            onEvent(NoteDetailsEvent.ToggleFontSizeDisplay)
                                            TextModificationEvent.IncreaseFont
                                        }

                                        5 -> {
                                            onEvent(NoteDetailsEvent.ToggleColorPicker)
                                            return@clickable
                                        }

                                        6 -> {
                                            TextModificationEvent.Align(AlignType.START)
                                        }

                                        7 -> {
                                            TextModificationEvent.Align(AlignType.CENTER)
                                        }

                                        8 -> {
                                            TextModificationEvent.Align(AlignType.END)
                                        }

                                        9 -> {
                                            TextModificationEvent.List
                                        }

                                        else -> return@clickable
                                    }
                                    onEvent(
                                        NoteDetailsEvent.ToggleSelectedOption(
                                            textModificationEvent,
                                            index
                                        )
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = option.iconId,
                                contentDescription = "Text Modification Option",
                                tint = if(state.selectableTextOptions[index] == true) {
                                    if(option.iconId == Icons.Default.FormatColorText) {
                                        color
                                    } else {
                                        Color.White
                                    }
                                } else {
                                    if(option.iconId == Icons.Default.FormatColorText) {
                                        color
                                    } else {
                                        Color.Black
                                    }
                                }
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(0.2f)
                        .padding(end = 5.dp, bottom = 10.dp)
                ) {
                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                launch {
                                    state.lockedIconInitialColor?.animateTo(
                                        targetValue = if(state.isNoteLocked) GreenValid else Color.Red,
                                        animationSpec = tween(1000)
                                    ) ?: newNoteLockBtnColor.animateTo(
                                        targetValue = if(state.isNoteLocked) GreenValid else Color.Red,
                                        animationSpec = tween(1000)
                                    )
                                }
                                showSnackBarWithCustomDuration(
                                    scope = scope,
                                    snackBarState = snackBarState,
                                    message = if(state.isNoteLocked) "Note is Unlocked" else "Note is Locked",
                                    duration = 1000L
                                )
                            }
                            onEvent(NoteDetailsEvent.LockNote)
                        },
                        containerColor = state.lockedIconInitialColor?.value ?: newNoteLockBtnColor.value,
                        contentColor = Color.White,
                        modifier = Modifier
                            .shadow(10.dp, RoundedCornerShape(10.dp))
                            .border(
                                width = 5.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Icon(
                            imageVector = if(state.isNoteLocked) Icons.Default.Lock else ImageVector.vectorResource(R.drawable.nolock),
                            contentDescription = "Lock A Note"
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    FloatingActionButton(
                        onClick = {
                            onEvent(NoteDetailsEvent.SaveNote)
                        },
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.save),
                            contentDescription = "Save Note"
                        )
                    }
                }
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(16.dp))
                TransparentTextField(
                    text = state.title,
                    hint = "Enter Note Title...",
                    onValueChange = {
                        onEvent(NoteDetailsEvent.ChangedTitle(it))
                    },
                    onFocusChange = {
                        onEvent(NoteDetailsEvent.ChangeTitleFocus(it))
                    },
                    isHintVisible = state.isTitleHintVisible,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp),
                    textStyle = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold)
                )
                HorizontalDivider(
                    modifier = Modifier.padding(top = 5.dp),
                    thickness = 2.dp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(20.dp))

                TransparentRichTextField(
                    richTextState = richTextState,
                    hint = "Enter Note Content...",
                    isHintVisible = state.isContentHintVisible,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp),
                    onFocusChange = {
                        onEvent(NoteDetailsEvent.ChangeContentFocus(it))
                    }
                )
            }

            AnimatedVisibility(
                visible = state.isFontSizeVisible,
                modifier = Modifier.align(Alignment.BottomCenter),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    Spacer(modifier = Modifier.width(185.dp))
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFF5F5DC)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Font: ${state.currentFontSize.value.toInt()}",
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if(state.isColorPickerDialogVisible) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(start = 10.dp, end = 10.dp)
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragEnd = {
                                        onEvent(
                                            NoteDetailsEvent.ToggleSelectedOption(
                                                textEvent = TextModificationEvent.ColorText(color),
                                                index = 5
                                            )
                                        )
                                    },
                                    onDragStart = {
                                        colorPickerCenter = it
                                    }
                                ) { change, dragAmount ->

                                    with(density) {
                                        colorPickerCenter = Offset(
                                            x = (colorPickerCenter.x + dragAmount.x)
                                                .coerceIn(
                                                    minimumValue = 25.dp.toPx(),
                                                    maximumValue = canvasWidth - 25.dp.toPx()
                                                ),
                                            y = 25.dp.toPx()
                                        )

                                        color = getColorAtPosition(
                                            position = change.position,
                                            width = canvasWidth,
                                            gradientColors = gradientColors
                                        )
                                    }
                                }
                            }
                    ) {

                        canvasWidth = size.width

                        drawRoundRect(
                            brush = Brush.horizontalGradient(
                                colors = gradientColors
                            ),
                            cornerRadius = CornerRadius(75f, 75f)
                        )
                        drawCircle(
                            color = Color.Black,
                            radius = 25.dp.toPx(),
                            center = if(colorPickerCenter == Offset.Zero) Offset(25.dp.toPx(), 25.dp.toPx()) else colorPickerCenter,
                            style = Stroke(width = 4.dp.toPx())
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}
