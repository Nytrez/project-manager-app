package com.example.projectmanager.data.model.projects.manage.project

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class ProjectChangeRequest (val projectId: Int,
                                 val projectName: String,
                                 val projectDescription: String,
                                 val projectStartDate: String,
                                 val projectEstimatedEndDate: String,
                                 val projectStatus: String
)
