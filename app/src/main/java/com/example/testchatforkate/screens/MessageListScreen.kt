package com.example.testchatforkate.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.testchatforkate.app.ThisApp
import com.example.testchatforkate.domain.model.Chat
import com.example.testchatforkate.domain.model.Collection
import com.example.testchatforkate.domain.model.Message
import com.example.testchatforkate.widgets.EditTextSend
import com.example.testchatforkate.widgets.SingleMessage
import com.example.testchatforkate.widgets.VideoMessage
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun MessageListScreen(
    navController: NavController,
    uid: String,
    name: String,
) {

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {

        } else {

        }
    }

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            navController.navigate(Screen.SelectedFileScreen.passUriAndChatUid(uri.toString(), chatUid = uid))
        }

    var messageState = remember {
        mutableStateOf("")
    }

    var messages: List<Message> by remember {
        mutableStateOf(listOf())
    }

    LaunchedEffect(key1 = Unit) {
        getMessages(uid) {
            messages = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 12.dp, end = 12.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(0.9f, fill = true),
            reverseLayout = true
        ) {
            items(messages) { message ->
                if (message.isFile) {
                    VideoMessage(message = message.text, isCurrentUser = message.sendBy == ThisApp.currentUser)
                    Spacer(modifier = Modifier.height(2.dp))
                } else {
                    SingleMessage(message = message.text, isCurrentUser = message.sendBy == ThisApp.currentUser)
                    Spacer(modifier = Modifier.height(2.dp))
                }

            }
        }
        Box(
            modifier = Modifier
                .weight(0.1f)
                .padding(6.dp)
        ) {
            EditTextSend(
                "enter message", modifier = Modifier
                    .fillMaxWidth(),
                state = messageState,
                {
                    addMessage(messageState.value, uid, name, navController)
                }, {
                    checkPermission(context, {
                        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
                    }, {
                        launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    })

                })
        }
    }
}

fun addMessage(message: String, chatUid: String, opponent: String, navController: NavController) {
    if (message.isNotEmpty()) {
        if (chatUid == " ") {
            createNewChat(message = message, opponentName = opponent, navController = navController)
        } else {
            Firebase.firestore.collection(Collection.CHATS.name).document(chatUid)
                .collection(Collection.MESSAGES.name)
                .add(
                    Message(
                        message,
                        ThisApp.currentUser,
                        System.currentTimeMillis(),
                    )
                ).addOnSuccessListener {

                }
        }

    }
}

fun getMessages(uid: String, block: (List<Message>) -> Unit) {
    Firebase.firestore.collection(Collection.CHATS.name).document(uid)
        .collection(Collection.MESSAGES.name)
        .orderBy("sendOn")
        .addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            var list = emptyList<Message>().toMutableList()
            if (value != null) {
                for (doc in value) {
                    val data = doc.data

                    list.add(
                        Message(
                            text = data["text"].toString(),
                            sendBy = data["sendBy"].toString(),
                            sendOn = data["sendOn"] as Long,
                            isFile = data["file"] as Boolean
                        )
                    )
                }
                block.invoke(list.asReversed())
            }
        }
}

fun createNewChat(opponentName: String, message: String, navController: NavController) {
    val ref = Firebase.firestore.collection(Collection.CHATS.name)
    ref.add(
        Chat(
            "",
            listOf(
                ThisApp.currentUser,
                opponentName
            )
        )
    ).addOnCompleteListener {
        val docUid = it.result.id
        ref.document(docUid).update("uid", docUid).addOnCompleteListener {
            ref.document(docUid).collection(Collection.MESSAGES.name).add(
                Message(
                    text = message, sendBy = ThisApp.currentUser, sendOn = System.currentTimeMillis()
                )
            )
            navController.navigate(Screen.MessageListScreen.passUidAndName(docUid, opponentName)) {
                popUpTo(Screen.ChannelListScreen.route) {
                    inclusive = false
                }
            }
        }
    }
}

fun checkPermission(
    context: Context,
    granted: () -> Unit,
    denied: () -> Unit
) {
    when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) -> {
            granted.invoke()
        }
        else -> {
            denied.invoke()
        }
    }
}



