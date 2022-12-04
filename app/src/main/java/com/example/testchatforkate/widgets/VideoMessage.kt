package com.example.testchatforkate.widgets

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VideoMessage(
    message: String,
    isCurrentUser: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            elevation = 1.dp,
            color = if (isCurrentUser) MaterialTheme.colors.primary else MaterialTheme.colors.surface
        ) {
            VideoPlayer(
                uri = Uri.parse(message), modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
            )
        }
    }
}