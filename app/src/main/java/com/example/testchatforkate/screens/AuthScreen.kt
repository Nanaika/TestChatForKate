package com.example.testchatforkate.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.testchatforkate.app.ThisApp
import com.example.testchatforkate.domain.model.Collection
import com.example.testchatforkate.domain.model.User
import com.example.testchatforkate.widgets.Button
import com.example.testchatforkate.widgets.EditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    navController: NavController
) {

    var textFieldState = remember {
        TextFieldState()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val scope = rememberCoroutineScope()
        EditText(hint = "enter name", modifier = Modifier, textFieldState = textFieldState)
        Spacer(modifier = Modifier.height(12.dp))
        Button {
            if (textFieldState.text == "") {
                textFieldState.onError = textFieldState.text == ""
                return@Button
            }
            scope.launch {
                Firebase.auth.signInWithEmailAndPassword(
                    "zloyx2@mail.ru", "123456"
                )
                setUser(name = textFieldState.text) {
                    ThisApp.currentUser = textFieldState.text

                    navController.navigate(Screen.ChannelListScreen.route)
                }
            }
        }
    }
}

fun setUser(name: String, block: () -> Unit) {
    val ref = Firebase.firestore.collection(Collection.USERS.name)
    getUser(name, {
        block.invoke()
    }, {
        ref.add(User(name)).addOnCompleteListener {
            val docUid = it.result.id
            ref.document(docUid).update("ID", docUid).addOnCompleteListener {
                block.invoke()
            }
        }
    })
}

fun getUser(name: String, found: (user: User) -> Unit, notFound: () -> Unit) {
    val ref = Firebase.firestore.collection(Collection.USERS.name)
    var foundUser = false
    ref.get().addOnCompleteListener { task ->
        val users = task.result.documents
        users.forEach {
            if (it["nickName"] == name) {
                foundUser = true
                found.invoke(
                    User(it.getField("nickName")!!)
                )
            }
        }
        if (!foundUser) notFound.invoke()
    }
}

class TextFieldState() {
    var text: String by mutableStateOf("")
    var onError: Boolean by mutableStateOf(false)
}






