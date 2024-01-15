package com.example.projectmanager.ui.dashboard.projects.create

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.data.api.RetrofitBuilder
import com.example.projectmanager.data.util.ResponseWrapper
import com.example.projectmanager.data.model.projects.manage.project.ProjectCreateRequest
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.ui.util.SessionManager
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class CreateProjectViewModel : ViewModel() {

    val LOG_TAG = "CreateProjectViewModel"

    val projectCreateResponse : MutableLiveData<Resource<Unit>> = MutableLiveData()

    public fun createProject(name : String, description : String, startDate : String, endDate : String) {
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
                handleTasksResponse(response)
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "addTaskToProject: ${t.message}")
                when (t) {
                    is IOException -> projectCreateResponse.postValue(Resource.Error("Network Failure"))
                    else -> projectCreateResponse.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }
    private fun handleTasksResponse(response: Response<ResponseWrapper<Unit>>) {
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {

                projectCreateResponse.postValue(Resource.Success(Unit))

            } else {
                projectCreateResponse.postValue(Resource.Error("Response body is null"))
            }
        } else {
            projectCreateResponse.postValue(Resource.Error(response.message()))
        }
    }
}