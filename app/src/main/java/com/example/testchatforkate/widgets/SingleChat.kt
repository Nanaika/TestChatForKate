package com.example.testchatforkate.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testchatforkate.domain.model.User
import kotlin.random.Random

@Composable
fun SingleChat(user: User, lastMessage: String, onClick: (name: String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick.invoke(user.nickName)
            }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .height(30.dp)
                .width(30.dp),
            shape = CircleShape,
            color = Color(
                Random.nextInt(25, 255),
                Random.nextInt(25, 255),
                Random.nextInt(25, 255)
            )
        ) {}
        Spacer(modifier = Modifier.width(12.dp))
        Column() {
            Text(text = user.nickName, style = MaterialTheme.typography.h6)
            Text(text = lastMessage, style = MaterialTheme.typography.body1)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyPreview() {
    SingleChat(user = User("Nanai"), lastMessage = "", onClick = {})
}
