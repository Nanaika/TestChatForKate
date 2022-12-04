package com.example.testchatforkate.screens

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.testchatforkate.app.ThisApp
import com.example.testchatforkate.domain.model.Collection
import com.example.testchatforkate.domain.model.Message
import com.example.testchatforkate.widgets.VideoPlayer
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

@Composable
fun SelectedFileScreen(fileUri: String, navController: NavController, chatUid: String) {
    Column(modifier = Modifier.fillMaxSize()) {
        VideoPlayer(
            uri = Uri.parse(fileUri),
            modifier = Modifier
                .weight(0.9f)
                .fillMaxSize(),
        )
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(0.1f)
        ) {
            IconButton(onClick = {
                save(Uri.parse(fileUri), chatUid = chatUid) {
                    navController.popBackStack()
                }
            }) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "")
            }
        }
    }
}

fun save(uri: Uri, chatUid: String, success: (url: String) -> Unit) {

    println("CURRENTFIREBASEUSER---------------   ${Firebase.auth.currentUser?.uid}")
    val ref = Firebase.storage.reference.child(ThisApp.currentUser + uri.lastPathSegment.toString())
    val uploadTask = ref.putFile(uri)

    val urlTask = uploadTask.continueWithTask { task ->
        if (!task.isSuccessful) {
            task.exception.let {
                throw it!!
            }
        }
        ref.downloadUrl
    }.addOnCompleteListener { task ->
        println("ENTERED SAVE AFTER UPLOAD FILE SAVE TO MESSAGES-------------------------")
        if (task.isSuccessful) {
            val downloadUri = task.result
            println("-----------url -----------------   ${downloadUri}")
            Firebase.firestore.collection(Collection.CHATS.name)
                .document(chatUid)
                .collection(Collection.MESSAGES.name)
                .add(
                    Message(
                        downloadUri.toString(),
                        ThisApp.currentUser,
                        System.currentTimeMillis(),
                        true
                    )
                ).addOnCompleteListener {
                    success.invoke(downloadUri.toString())
                }
        }
    }
}