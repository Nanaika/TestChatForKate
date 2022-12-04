package com.example.testchatforkate.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.rounded.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier

@Composable
fun EditTextSend(
    hint: String,
    modifier: Modifier,
    state: MutableState<String>,
    onSendClick: () -> Unit,
    onPickClick: () -> Unit
) {
    OutlinedTextField(
        value = state.value,
        onValueChange = {
            state.value = it
        },
        placeholder = { Text(text = hint) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        leadingIcon = {
            IconButton(onClick = {
                onPickClick.invoke()
            }) {
                Icon(imageVector = Icons.Rounded.Image, contentDescription = "", tint = MaterialTheme.colors.primary)
            }
        },
        trailingIcon = {
            IconButton(onClick = {
                onSendClick.invoke()
            }) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "", tint = MaterialTheme.colors.primary)
            }
        }
    )
}