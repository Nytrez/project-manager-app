package com.example.projectmanager.data.model.tasks.manage

import java.util.Date

data class TaskUpdateRequest (
                            val taskId : Int,
                            val taskDescriptionShort : String?,
                            val taskHeader : String?,
                            val taskDescription : String?,
                            val responsiblePersonId : Int?,
                            val priority : Int?,
                            val status : Int?,
                            val dueDate : String?,
                            val completionDate : String?
)