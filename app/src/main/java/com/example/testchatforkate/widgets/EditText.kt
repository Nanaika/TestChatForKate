package com.example.testchatforkate.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.testchatforkate.screens.TextFieldState

@Composable
fun EditText(
    hint: String,
    modifier: Modifier,
    textFieldState: TextFieldState = remember {
        TextFieldState()
    }
) {
    OutlinedTextField(
        value = textFieldState.text,
        onValueChange = {
            textFieldState.text = it
        },
        placeholder = { Text(text = hint, style = MaterialTheme.typography.body1) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        isError = textFieldState.onError
    )
}