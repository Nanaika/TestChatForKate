package com.example.testchatforkate.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.testchatforkate.app.ThisApp
import com.example.testchatforkate.domain.model.Collection
import com.example.testchatforkate.domain.model.User
import com.example.testchatforkate.widgets.SearchView
import com.example.testchatforkate.widgets.SingleChat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun AddChatScreen(
    navController: NavController
) {

    var users: List<User> by remember {
        mutableStateOf(listOf())
    }

    LaunchedEffect(key1 = Unit) {
        getUsers() {
            users = it
        }
    }

    var searchState = remember {
        mutableStateOf(TextFieldValue(""))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier.padding(6.dp)
        ) {
            SearchView(state = searchState)
        }
        LazyColumn() {
            items(users) {
                SingleChat(user = User(it.nickName), lastMessage = "", onClick = {
                    navController.navigate(Screen.MessageListScreen.passUidAndName(" ", it))
                })
            }
        }
    }
}//end of screen

fun getUsers(block: (List<User>) -> Unit) {
    val userList = mutableListOf<User>()
    Firebase.firestore.collection(Collection.USERS.name).get().addOnCompleteListener {
        if (it.isSuccessful) {
            it.result.documents.forEach {
                if (it["nickName"] != ThisApp.currentUser) {
                    userList.add(User(it["nickName"] as String))
                }
            }
            block.invoke(userList)
        }
    }
}