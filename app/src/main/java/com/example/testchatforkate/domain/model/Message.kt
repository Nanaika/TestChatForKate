package com.example.testchatforkate.domain.model

data class Message(
    val text: String,
    val sendBy: String,
    val sendOn: Long,
    val isFile: Boolean = false
)
