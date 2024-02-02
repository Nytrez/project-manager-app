package com.example.projectmanager.data.model.projects.manage.project

data class ProjectCreateRequest(
    val projectName: String,
    val projectDescription: String,
    val projectStartDate: String,
    val projectEstimatedEndDate: String,
    val projectStatus: String? = "INACTIVE"

)