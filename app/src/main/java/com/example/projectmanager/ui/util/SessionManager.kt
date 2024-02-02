package com.example.projectmanager.ui.util

object SessionManager {
    private var authToken: String? = null
    fun saveAuthToken(token: String) {
        authToken = "Bearer $token"
    }

    fun fetchAuthToken(): String? {
        return authToken
    }
}