package com.example.testchatforkate.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi

@UnstableApi
@Composable
fun SingleMessage(
    message: String,
    isCurrentUser: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            backgroundColor = if (isCurrentUser) MaterialTheme.colors.primary else MaterialTheme.colors.background,
            modifier = Modifier.fillMaxWidth(0.75f)
        ) {
            Text(
                text = message,
                textAlign = if (isCurrentUser) TextAlign.End else TextAlign.Start,
                color = if (!isCurrentUser) MaterialTheme.colors.primary else MaterialTheme.colors.background,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}