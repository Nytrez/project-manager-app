package com.example.projectmanager.ui.dashboard.projects.manage.details.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.data.api.RetrofitBuilder
import com.example.projectmanager.data.model.projects.ProjectResponseItem
import com.example.projectmanager.data.model.projects.manage.project.ProjectChangeRequest
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.ui.util.SessionManager
import com.example.projectmanager.ui.util.handleApiResponse
import kotlinx.coroutines.launch
import java.io.IOException

class ProjectManageViewModel : ViewModel() {

    val LOG_TAG = "TaskDetailsViewModel"

    val projectDetails: MutableLiveData<Resource<ProjectResponseItem>> = MutableLiveData()
    val projectChangeResponse: MutableLiveData<Resource<Unit>> = MutableLiveData()
    val projectDeletionResponse: MutableLiveData<Resource<Unit>> = MutableLiveData()

    fun getProjectDetails(projectId: Int) {
        viewModelScope.launch {
            try {
                projectDetails.postValue(Resource.Loading())
                //Log.d("TasksViewModel", "getAllProjectTasks: $userToken")
                val response = RetrofitBuilder.api.getProjectDetails(SessionManager.fetchAuthToken()!!, projectId)
                handleApiResponse(response, projectDetails)
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "error whhile requesting tasks: $t")
                when (t) {
                    is IOException -> projectDetails.postValue(Resource.Error("Network Failure"))
                    else -> projectDetails.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }


    fun changeProjectDetails(
        projectId: Int, newName: String, newDescription: String, newStartDate: String, newEndDate: String,
        newStatus: String
    ) {
        viewModelScope.launch {
            try {
                projectChangeResponse.postValue(Resource.Loading())

                val projectRequest = ProjectChangeRequest(
                    projectId = projectId,
                    projectName = newName,
                    projectDescription = newDescription,
                    projectStartDate = newStartDate,
                    projectEstimatedEndDate = newEndDate,
                    projectStatus = newStatus,
                )

                val response = RetrofitBuilder.api.changeProjectDetails(SessionManager.fetchAuthToken()!!, projectRequest)
                handleApiResponse(response, projectChangeResponse)
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "error whhile requesting comments: $t")
                when (t) {
                    is IOException -> projectChangeResponse.postValue(Resource.Error("Network Failure"))
                    else -> projectChangeResponse.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }

    fun removeProject(projectId: Int) {
        viewModelScope.launch {
            try {
                projectDeletionResponse.postValue(Resource.Loading())
                //Log.d("TasksViewModel", "getAllProjectTasks: $userToken")
                val response = RetrofitBuilder.api.deleteProject(SessionManager.fetchAuthToken()!!, projectId)
                handleApiResponse(response, projectDeletionResponse)
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "error whhile requesting tasks: $t")
                when (t) {
                    is IOException -> projectDeletionResponse.postValue(Resource.Error("Network Failure"))
                    else -> projectDeletionResponse.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }

}

