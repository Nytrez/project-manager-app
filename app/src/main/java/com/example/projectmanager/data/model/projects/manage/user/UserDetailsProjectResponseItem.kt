package com.example.projectmanager.data.model.projects.manage.user

data class UserDetailsProjectResponseItem(
    val userId: Int,
    val name: String,
    val surname: String,
    val email: String,
    val role: String,
    val permissions: Int,
)