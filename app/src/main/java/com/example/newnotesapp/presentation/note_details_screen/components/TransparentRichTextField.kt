package com.example.newnotesapp.presentation.note_details_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.BasicRichTextEditor

@Composable
fun TransparentRichTextField(
    richTextState: RichTextState,
    hint: String,
    modifier: Modifier = Modifier,
    isHintVisible: Boolean,
    onFocusChange: (FocusState) -> Unit,
    hintColor: Color = Color.DarkGray
) {
    Box(modifier = modifier){
        BasicRichTextEditor(
            state = richTextState,
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    onFocusChange(it)
                },
            textStyle = TextStyle(fontSize = 15.sp)
        )
        if(isHintVisible){
            Text(
                text = hint,
                color = hintColor,
                fontSize = 20.sp
            )
        }

    }
}