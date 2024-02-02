package com.example.projectmanager.data.model.projects.manage.project

data class ProjectChangeRequest(
    val projectId: Int,
    val projectName: String,
    val projectDescription: String,
    val projectStartDate: String,
    val projectEstimatedEndDate: String,
    val projectStatus: String
)
