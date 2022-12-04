package com.example.testchatforkate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.testchatforkate.screens.*
import com.example.testchatforkate.ui.theme.TestChatForKateTheme
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Firebase.initialize(this)
        setContent {
            TestChatForKateTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    ChatApp()
                }
            }
        }
    }
}

@Composable
fun ChatApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.AuthScreen.route) {
        composable(Screen.ChannelListScreen.route) {
            ChannelListScreen(navController = navController)
        }
        composable(
            route = Screen.SelectedFileScreen.route,
            arguments = listOf(navArgument("uri") {
                type = NavType.StringType
                defaultValue = " "
            },
                navArgument("chatUid") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            SelectedFileScreen(
                navController = navController,
                fileUri = it.arguments?.getString("uri")!!,
                chatUid = it.arguments?.getString("chatUid")!!
            )
        }
        composable(Screen.AuthScreen.route) {
            AuthScreen(navController = navController)
        }
        composable(Screen.AddChatScreen.route) {
            AddChatScreen(navController = navController)
        }
        composable(
            route = Screen.MessageListScreen.route,
            arguments = listOf(navArgument("uid") {
                type = NavType.StringType
                defaultValue = " "
            },
                navArgument("name") {
                    type = NavType.StringType
                    defaultValue = " "
                }
            )
        ) {
            MessageListScreen(
                navController = navController,
                uid = it.arguments?.getString("uid")!!,
                name = it.arguments?.getString("name")!!
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TestChatForKateTheme {
        ChatApp()
    }
}
