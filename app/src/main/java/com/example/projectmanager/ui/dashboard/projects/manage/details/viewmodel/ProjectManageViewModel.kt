package com.example.projectmanager.ui.dashboard.projects.manage.details.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.data.api.RetrofitBuilder
import com.example.projectmanager.data.util.ResponseWrapper
import com.example.projectmanager.data.model.projects.manage.project.ProjectChangeRequest
import com.example.projectmanager.data.model.projects.ProjectResponseItem
import com.example.projectmanager.data.model.projects.manage.project.ProjectDeleteRequest
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.ui.util.SessionManager
import kotlinx.coroutines.launch
import retrofit2.Response
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
                handleProjectDetailsResponse(response)
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
                handleProjectChangeResponse(response)
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "error whhile requesting comments: $t")
                when (t) {
                    is IOException -> projectChangeResponse.postValue(Resource.Error("Network Failure"))
                    else -> projectChangeResponse.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }

    private fun handleProjectChangeResponse(response: Response<ResponseWrapper<Unit>>) {
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                if (responseBody.body != null) {
                    projectChangeResponse.postValue(Resource.Success(responseBody.body))
                } else {
                    projectChangeResponse.postValue(Resource.Error(responseBody.reason))
                }
            } else {
                projectChangeResponse.postValue(Resource.Error("Response body is null"))
            }
        } else {
            projectChangeResponse.postValue(Resource.Error(response.message()))
        }
    }

    private fun handleProjectDetailsResponse(response: Response<ResponseWrapper<ProjectResponseItem>>) {
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                if (responseBody.body != null) {
                    projectDetails.postValue(Resource.Success(responseBody.body))
                } else {
                    projectDetails.postValue(Resource.Error(responseBody.reason))
                }
            } else {
                projectDetails.postValue(Resource.Error("Response body is null"))
            }
        } else {
            projectDetails.postValue(Resource.Error(response.message()))
        }
    }

    fun removeProject(projectId: Int) {
        viewModelScope.launch {
            try {
                projectDeletionResponse.postValue(Resource.Loading())
                //Log.d("TasksViewModel", "getAllProjectTasks: $userToken")
                val projectRequest = ProjectDeleteRequest(
                    projectId = projectId
                )
                val response = RetrofitBuilder.api.deleteProject(SessionManager.fetchAuthToken()!!, projectRequest)
                handleProjectDeleteResponse(response)
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "error whhile requesting tasks: $t")
                when (t) {
                    is IOException -> projectDeletionResponse.postValue(Resource.Error("Network Failure"))
                    else -> projectDeletionResponse.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }

    private fun handleProjectDeleteResponse(response: Response<ResponseWrapper<Unit>>) {
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                if (responseBody.body != null) {
                    projectDeletionResponse.postValue(Resource.Success(responseBody.body))
                } else {
                    projectDeletionResponse.postValue(Resource.Error(responseBody.reason))
                }
            } else {
                projectDeletionResponse.postValue(Resource.Error("Response body is null"))
            }
        } else {
            projectDeletionResponse.postValue(Resource.Error(response.message()))
        }
    }
}

