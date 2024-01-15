package com.example.projectmanager.data.model.projects

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class ProjectResponseItem (
    @field:SerializedName("projectId")
    var projectId: Int,
    @field:SerializedName("projectName")
    var projectName: String,
    @field:SerializedName("projectDescription")
    var projectDescription: String,
    @field:SerializedName("projectStartDate")
    var projectStartDate: String,
    @field:SerializedName("projectEstimatedEndDate")
    var projectEstimatedEndDate: String,
    @field:SerializedName("projectStatus")
    var projectStatus: String,
    ) : Serializable
