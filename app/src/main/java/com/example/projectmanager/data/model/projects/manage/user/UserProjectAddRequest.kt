package com.example.projectmanager.data.model.projects.manage.user

data class UserProjectAddRequest(
    val projectId: Int,
    val email: String,
    val role: String,
    val permissions: Int,
)