package com.example.projectmanager.data.model.tasks

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TasksResponseItem(@field:SerializedName("taskId")
                                    var taskId: Int,
                             @field:SerializedName("projectId")
                                    var taskProjectId: Int,
                             @field:SerializedName("taskDescriptionShort")
                                    var taskDescriptionShort: String,
                             @field:SerializedName("taskHeader")
                                    var taskHeader: String,
                             @field:SerializedName("taskDescription")
                                    var taskDescription: String,
                             @field:SerializedName("responsiblePersonId")
                                    var taskResponsiblePersonId: Int? = null,
                             @field:SerializedName("priority")
                                    var taskPriority: Int,
                             @field:SerializedName("status")
                                    var taskStatus: Int,
                             @field:SerializedName("dueDate")
                                    var dueDate: Date? = null,
                             @field:SerializedName("completionDate")
                                    var completionDate: Date? = null,
)