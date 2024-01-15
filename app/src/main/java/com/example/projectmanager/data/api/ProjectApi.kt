package com.example.projectmanager.data.api

import com.example.projectmanager.data.util.ResponseWrapper
import com.example.projectmanager.data.model.login.LoginRequest
import com.example.projectmanager.data.model.login.LoginResponseItem
import com.example.projectmanager.data.model.login.RegisterRequest
import com.example.projectmanager.data.model.projects.manage.user.UserDetailsChangeProjectRequest
import com.example.projectmanager.data.model.projects.manage.user.UserDetailsProjectResponse
import com.example.projectmanager.data.model.projects.manage.project.ProjectChangeRequest
import com.example.projectmanager.data.model.projects.manage.project.ProjectCreateRequest
import com.example.projectmanager.data.model.projects.ProjectResponseItem
import com.example.projectmanager.data.model.tasks.TasksResponse
import com.example.projectmanager.data.model.projects.ProjectsResponse
import com.example.projectmanager.data.model.projects.manage.project.ProjectDeleteRequest
import com.example.projectmanager.data.model.projects.manage.project.ProjectRequest
import com.example.projectmanager.data.model.projects.manage.user.UserProjectAddRequest
import com.example.projectmanager.data.model.projects.manage.user.UserProjectRemoveRequest
import com.example.projectmanager.data.model.tasks.manage.TasksCreateRequest
import com.example.projectmanager.data.model.tasks.TasksResponseItem
import com.example.projectmanager.data.model.tasks.comments.TaskCommentRequest
import com.example.projectmanager.data.model.tasks.comments.TaskCommentsResponse
import com.example.projectmanager.data.model.tasks.manage.TaskDetailsRequest
import com.example.projectmanager.data.model.tasks.manage.TaskRemoveRequest
import com.example.projectmanager.data.model.tasks.manage.TaskUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ProjectApi {

    // User related endpoints
    @POST("api/v1/user/authentication/login")
    suspend fun login(@Body request: LoginRequest): Response<ResponseWrapper<LoginResponseItem>>

    @POST("api/v1/user/register")
    suspend fun register(@Body request: RegisterRequest): Response<ResponseWrapper<Unit>>

    // Project related endpoints
    @POST("api/v1/project/getAllProjects")
    suspend fun getAllProjects(@Header("Authorization") token: String): Response<ResponseWrapper<ProjectsResponse>>

    @POST("api/v1/project/getAllProjectsOwner")
    suspend fun getAllProjectsOwner(@Header("Authorization") token: String): Response<ResponseWrapper<ProjectsResponse>>

    @POST("api/v1/project/getProjectDetails")
    suspend fun getProjectDetails(@Header("Authorization") token: String, @Query("projectId") projectId: Int):
            Response<ResponseWrapper<ProjectResponseItem>>

    @POST("api/v1/project/updateProject")
    suspend fun changeProjectDetails(@Header("Authorization") token: String, @Body project : ProjectChangeRequest):
            Response<ResponseWrapper<Unit>>

    @POST("api/v1/project/createProject")
    suspend fun createProject(@Header("Authorization") token: String, @Body request: ProjectCreateRequest): Response<ResponseWrapper<Unit>>

    @POST("api/v1/project/deleteProject")
    suspend fun deleteProject(@Header("Authorization") token: String, @Body project : ProjectDeleteRequest):
            Response<ResponseWrapper<Unit>>

    @POST("api/v1/project/getProjectUsers")
    suspend fun getProjectUsers(@Header("Authorization") token: String, @Body project : ProjectRequest):
            Response<ResponseWrapper<UserDetailsProjectResponse>>

    @POST("api/v1/project/updateProjectUserDetails")
    suspend fun changeProjectUserDetails(@Header("Authorization") token: String, @Body project : UserDetailsChangeProjectRequest):
            Response<ResponseWrapper<Unit>>

    @POST("api/v1/project/addUserToProject")
    suspend fun addUserToProject(@Header("Authorization") token: String, @Body project : UserProjectAddRequest):
            Response<ResponseWrapper<Unit>>

    @POST("api/v1/project/removeUserFromProject")
    suspend fun removeUserFromProject(@Header("Authorization") token: String, @Body project : UserProjectRemoveRequest):
            Response<ResponseWrapper<Unit>>

    // Task related endpoints
    @POST("api/v1/tasks/getProjectAllTasks")
    suspend fun getProjectAllTasks(@Header("Authorization") token: String, @Query("projectId") projectId : Int):
            Response<ResponseWrapper<TasksResponse>>

    @POST("api/v1/tasks/getTaskDetails")
    suspend fun getTaskDetails(@Header("Authorization") token: String, @Body task : TaskDetailsRequest):
            Response<ResponseWrapper<TasksResponseItem>>

    @POST("api/v1/tasks/addTaskToProject")
    suspend fun addTaskToProject(@Header("Authorization") token: String, @Body task : TasksCreateRequest):
            Response<ResponseWrapper<Unit>>

    @POST("api/v1/tasks/updateTaskInProject")
    suspend fun updateTaskInProject(@Header("Authorization") token: String, @Body task : TaskUpdateRequest):
            Response<ResponseWrapper<Unit>>

    @POST("api/v1/tasks/removeTaskFromProject")
    suspend fun removeTaskFromProject(@Header("Authorization") token: String, @Body task : TaskRemoveRequest):
            Response<ResponseWrapper<Unit>>

    // Comment related endpoints
    @POST("api/v1/comments/getTaskAllComments")
    suspend fun getTaskAllComments(@Header("Authorization") token: String, @Query("taskId") taskId : Int):
            Response<ResponseWrapper<TaskCommentsResponse>>

    @POST("api/v1/comments/addCommentToTask")
    suspend fun addCommentToTask(@Header("Authorization") token: String, @Body task : TaskCommentRequest):
            Response<ResponseWrapper<Unit>>

}