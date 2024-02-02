package com.example.projectmanager.data.api

import com.example.projectmanager.data.model.login.LoginRequest
import com.example.projectmanager.data.model.login.LoginResponseItem
import com.example.projectmanager.data.model.login.RegisterRequest
import com.example.projectmanager.data.model.projects.ProjectResponseItem
import com.example.projectmanager.data.model.projects.ProjectsResponse
import com.example.projectmanager.data.model.projects.manage.project.ProjectChangeRequest
import com.example.projectmanager.data.model.projects.manage.project.ProjectCreateRequest
import com.example.projectmanager.data.model.projects.manage.user.UserDetailsChangeProjectRequest
import com.example.projectmanager.data.model.projects.manage.user.UserDetailsProjectResponse
import com.example.projectmanager.data.model.projects.manage.user.UserProjectAddRequest
import com.example.projectmanager.data.model.tasks.TasksResponse
import com.example.projectmanager.data.model.tasks.TasksResponseItem
import com.example.projectmanager.data.model.tasks.comments.TaskCommentRequest
import com.example.projectmanager.data.model.tasks.comments.TaskCommentsResponse
import com.example.projectmanager.data.model.tasks.manage.TaskUpdateRequest
import com.example.projectmanager.data.model.tasks.manage.TasksCreateRequest
import com.example.projectmanager.data.util.ResponseWrapper
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
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
    @GET("api/v1/project/getAllProjects")
    suspend fun getAllProjects(@Header("Authorization") token: String): Response<ResponseWrapper<ProjectsResponse>>

    @GET("api/v1/project/getAllProjectsOwner")
    suspend fun getAllProjectsOwner(@Header("Authorization") token: String): Response<ResponseWrapper<ProjectsResponse>>

    @GET("api/v1/project/getProjectDetails")
    suspend fun getProjectDetails(@Header("Authorization") token: String, @Query("projectId") projectId: Int):
            Response<ResponseWrapper<ProjectResponseItem>>

    @POST("api/v1/project/updateProject")
    suspend fun changeProjectDetails(@Header("Authorization") token: String, @Body project: ProjectChangeRequest):
            Response<ResponseWrapper<Unit>>

    @POST("api/v1/project/createProject")
    suspend fun createProject(@Header("Authorization") token: String, @Body request: ProjectCreateRequest): Response<ResponseWrapper<Unit>>

    @DELETE("api/v1/project/deleteProject")
    suspend fun deleteProject(@Header("Authorization") token: String, @Query("projectId") projectId: Int):
            Response<ResponseWrapper<Unit>>

    @GET("api/v1/project/getProjectUsers")
    suspend fun getProjectUsers(@Header("Authorization") token: String, @Query("projectId") projectId: Int):
            Response<ResponseWrapper<UserDetailsProjectResponse>>

    @POST("api/v1/project/updateProjectUserDetails")
    suspend fun changeProjectUserDetails(@Header("Authorization") token: String, @Body project: UserDetailsChangeProjectRequest):
            Response<ResponseWrapper<Unit>>

    @POST("api/v1/project/addUserToProject")
    suspend fun addUserToProject(@Header("Authorization") token: String, @Body project: UserProjectAddRequest):
            Response<ResponseWrapper<Unit>>

    @DELETE("api/v1/project/removeUserFromProject")
    suspend fun removeUserFromProject(
        @Header("Authorization") token: String, @Query("projectId") projectId: Int, @Query("userId") userId:
        Int
    ):
            Response<ResponseWrapper<Unit>>

    // Task related endpoints
    @GET("api/v1/tasks/getProjectAllTasks")
    suspend fun getProjectAllTasks(@Header("Authorization") token: String, @Query("projectId") projectId: Int):
            Response<ResponseWrapper<TasksResponse>>

    @GET("api/v1/tasks/getTaskDetails")
    suspend fun getTaskDetails(@Header("Authorization") token: String, @Query("taskId") taskId: Int):
            Response<ResponseWrapper<TasksResponseItem>>

    @POST("api/v1/tasks/addTaskToProject")
    suspend fun addTaskToProject(@Header("Authorization") token: String, @Body task: TasksCreateRequest):
            Response<ResponseWrapper<Unit>>

    @POST("api/v1/tasks/updateTaskInProject")
    suspend fun updateTaskInProject(@Header("Authorization") token: String, @Body task: TaskUpdateRequest):
            Response<ResponseWrapper<Unit>>

    @DELETE("api/v1/tasks/removeTaskFromProject")
    suspend fun removeTaskFromProject(@Header("Authorization") token: String, @Query("taskId") taskId: Int):
            Response<ResponseWrapper<Unit>>

    // Comment related endpoints
    @GET("api/v1/comments/getTaskAllComments")
    suspend fun getTaskAllComments(@Header("Authorization") token: String, @Query("taskId") taskId: Int):
            Response<ResponseWrapper<TaskCommentsResponse>>

    @POST("api/v1/comments/addCommentToTask")
    suspend fun addCommentToTask(@Header("Authorization") token: String, @Body task: TaskCommentRequest):
            Response<ResponseWrapper<Unit>>

    @POST("api/v1/comments/delCommentInTask")
    suspend fun delCommentInTask(@Header("Authorization") token: String, @Query("commentId") commentId: Int):
            Response<ResponseWrapper<Unit>>

}