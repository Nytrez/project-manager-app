package com.example.projectmanager.data.model.tasks.manage

data class TaskUpdateRequest(
    val taskId: Int,
    val taskDescriptionShort: String?,
    val taskHeader: String?,
    val taskDescription: String?,
    val responsiblePersonEmail: String?,
    val priority: Int?,
    val status: Int?,
    val dueDate: String?,
    val completionDate: String?
)