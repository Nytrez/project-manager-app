package com.example.projectmanager.data.model.tasks.manage

import java.util.Date

data class TasksCreateRequest(
    val projectId: Int,
    val taskDescriptionShort: String,
    val taskHeader: String,
    val taskDescription: String,
    val responsiblePersonId: Int? = null,
    val priority: Int,
    val status: Int = 0,
    val dueDate: Date? = null, // TODO
    val completionDate: Date? = null
)