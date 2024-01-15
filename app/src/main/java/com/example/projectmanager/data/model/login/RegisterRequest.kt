package com.example.projectmanager.data.model.login

data class RegisterRequest (
    val email: String,
    val password: String,
    val name: String,
    val surname: String
)