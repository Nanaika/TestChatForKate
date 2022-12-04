package com.example.testchatforkate.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.testchatforkate.app.ThisApp
import com.example.testchatforkate.domain.model.Chat
import com.example.testchatforkate.domain.model.Collection
import com.example.testchatforkate.domain.model.User
import com.example.testchatforkate.widgets.SearchView
import com.example.testchatforkate.widgets.SingleChat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun ChannelListScreen(
    navController: NavController
) {

    var chats: List<Chat> by remember {
        mutableStateOf(listOf())
    }

    LaunchedEffect(key1 = Unit) {
        getChats(ThisApp.currentUser) {
            chats = it
        }
    }

    var searchState = remember {
        mutableStateOf(TextFieldValue(""))
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddChatScreen.route)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = "")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
        ) {
            Box(
                modifier = Modifier.padding(6.dp)
            ) {
                SearchView(state = searchState)
            }
            LazyColumn() {
                itemsIndexed(chats) { index, item ->
                    var opponent = " "
                    item.users.forEach {
                        if (it != ThisApp.currentUser) {
                            opponent = it
                        }
                    }
                    println("oppenent -------   $opponent")
                    SingleChat(user = User(opponent), lastMessage = "", onClick = {
                        println("!!!!----uid and name    ${chats[index].uid}    $opponent")
                        navController.navigate(Screen.MessageListScreen.passUidAndName(chats[index].uid, opponent))
                    })
                }
            }
        }
    }
}//end of screen

fun getChats(name: String, block: (List<Chat>) -> Unit) {
    val chatList = mutableListOf<Chat>()
    Firebase.firestore.collection(Collection.CHATS.name)
        .whereArrayContains("users", name)
        .get()
        .addOnCompleteListener {
            if (it.isSuccessful) {
                it.result.documents.forEach {
                    chatList.add(Chat(it["uid"] as String, it["users"] as List<String>))
                }
                block.invoke(chatList)
            }
        }
}

