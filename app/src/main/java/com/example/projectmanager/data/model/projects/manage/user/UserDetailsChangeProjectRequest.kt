package com.example.projectmanager.data.model.projects.manage.user

data class UserDetailsChangeProjectRequest(val projectId: Int, val role: String, val permissions: Int, val userId: Int)