package com.example.projectmanager.ui.dashboard.projects.create.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.data.api.RetrofitBuilder
import com.example.projectmanager.data.model.projects.manage.project.ProjectCreateRequest
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.ui.util.SessionManager
import com.example.projectmanager.ui.util.handleApiResponse
import kotlinx.coroutines.launch
import java.io.IOException

class CreateProjectViewModel : ViewModel() {

    val LOG_TAG = "CreateProjectViewModel"

    val projectCreateResponse: MutableLiveData<Resource<Unit>> = MutableLiveData()

    fun createProject(name: String, description: String, startDate: String, endDate: String) {
        viewModelScope.launch {
            try {
                projectCreateResponse.postValue(Resource.Loading())

                val projectRequest = ProjectCreateRequest(
                    projectName = name,
                    projectDescription = description,
                    projectStartDate = startDate,
                    projectEstimatedEndDate = endDate
                )
                Log.d(LOG_TAG, "addTaskToProject: $projectRequest")

                val response = RetrofitBuilder.api.createProject(SessionManager.fetchAuthToken()!!, projectRequest)
                Log.d(LOG_TAG, "addTaskToProject: $response")
                handleApiResponse(response, projectCreateResponse)
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "addTaskToProject: ${t.message}")
                when (t) {
                    is IOException -> projectCreateResponse.postValue(Resource.Error("Network Failure"))
                    else -> projectCreateResponse.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }
}