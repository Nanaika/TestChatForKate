package com.example.testchatforkate.app

import android.app.Application

class ThisApp: Application() {

    companion object {
        lateinit var currentUser: String
    }

    override fun onCreate() {
        super.onCreate()
        currentUser = ""
    }

}