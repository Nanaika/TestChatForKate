package com.example.testchatforkate.screens

sealed class Screen(
    val route: String
) {
    object AuthScreen : Screen(
        route = "auth_screen"
    )

    object ChannelListScreen : Screen(
        route = "channel_list_screen"
    )

    object SelectedFileScreen : Screen(
        route = "selected_file_screen?uri={uri}&chatUid={chatUid}"
    ) {
        fun passUriAndChatUid(uri: String, chatUid: String): String {
            return "selected_file_screen?uri=${uri}&chatUid=${chatUid}"
        }
    }

    object MessageListScreen : Screen(
        route = "message_list_screen?uid={uid}&name={name}"
    ) {
        fun passUidAndName(uid: String, name: String): String {
            return "message_list_screen?uid=${uid}&name=${name}"
        }
    }

    object AddChatScreen : Screen(
        route = "add_chat_screen"
    )
}
