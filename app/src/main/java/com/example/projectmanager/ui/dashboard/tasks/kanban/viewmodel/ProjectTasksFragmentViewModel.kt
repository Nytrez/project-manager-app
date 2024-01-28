package com.example.projectmanager.ui.dashboard.tasks.kanban.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.data.api.RetrofitBuilder
import com.example.projectmanager.data.util.ResponseWrapper
import com.example.projectmanager.data.model.tasks.manage.TasksCreateRequest
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.ui.util.SessionManager
import com.example.projectmanager.ui.util.handleApiResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ProjectTasksFragmentViewModel : ViewModel() {

    val taskCreateResponse : MutableLiveData<Resource<Unit>> = MutableLiveData()

    public fun addTaskToProject(projectId: Int,  header: String, description: String, descriptionShort: String, priority: Int) {
        viewModelScope.launch {
            try {
                taskCreateResponse.postValue(Resource.Loading())

                val taskRequest = TasksCreateRequest(
                    projectId = projectId,
                    taskHeader = header,
                    taskDescriptionShort = descriptionShort,
                    taskDescription = description,
                    priority = priority
                )
                Log.d("ProjectTasksFragmentViewModel", "addTaskToProject: $taskRequest")

                val response = RetrofitBuilder.api.addTaskToProject(SessionManager.fetchAuthToken()!!, taskRequest)
                Log.d("ProjectTasksFragmentViewModel", "addTaskToProject: $response")
                handleApiResponse(response, taskCreateResponse)
            } catch (t: Throwable) {
                Log.d("ProjectTasksFragmentViewModel", "addTaskToProject: ${t.message}")
                when (t) {
                    is IOException -> taskCreateResponse.postValue(Resource.Error("Network Failure"))
                    else -> taskCreateResponse.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }
}