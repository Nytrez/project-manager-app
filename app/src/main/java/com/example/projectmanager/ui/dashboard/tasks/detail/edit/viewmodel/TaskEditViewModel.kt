package com.example.projectmanager.ui.dashboard.tasks.detail.edit.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.data.api.RetrofitBuilder
import com.example.projectmanager.data.model.tasks.TasksResponseItem
import com.example.projectmanager.data.model.tasks.manage.TaskUpdateRequest
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.ui.util.SessionManager
import com.example.projectmanager.ui.util.handleApiResponse
import kotlinx.coroutines.launch
import java.io.IOException

class TaskEditViewModel : ViewModel() {

    val LOG_TAG = "TaskEditViewModel"

    val taskDetailsResponse: MutableLiveData<Resource<TasksResponseItem>> = MutableLiveData()
    val taskEditResponse: MutableLiveData<Resource<Unit>> = MutableLiveData()
    val taskDeleteResponse: MutableLiveData<Resource<Unit>> = MutableLiveData()

    fun getTaskDetails(taskId: Int) {
        viewModelScope.launch {
            try {
                taskDetailsResponse.postValue(Resource.Loading())
                //Log.d("TasksViewModel", "getAllProjectTasks: $userToken")
                //val task = TaskDetailsRequest(taskId)
                val response = RetrofitBuilder.api.getTaskDetails(SessionManager.fetchAuthToken()!!, taskId)
                handleApiResponse(response, taskDetailsResponse)
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "error whhile requesting tasks: $t")
                when (t) {
                    is IOException -> taskDetailsResponse.postValue(Resource.Error("Network Failure"))
                    else -> taskDetailsResponse.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            try {
                taskDeleteResponse.postValue(Resource.Loading())
                //Log.d("TasksViewModel", "getAllProjectTasks: $userToken")
                val response = RetrofitBuilder.api.removeTaskFromProject(SessionManager.fetchAuthToken()!!, taskId)
                handleApiResponse(response, taskDeleteResponse)
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "error whhile requesting tasks: $t")
                when (t) {
                    is IOException -> taskDeleteResponse.postValue(Resource.Error("Network Failure"))
                    else -> taskDeleteResponse.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }


    fun updateTask(
        taskId: Int, taskDescriptionShort: String?, taskHeader: String?, taskDescription: String?, responsiblePersonEmail: String?,
        priority:
        Int?,
        status: Int?, dueDate: String?, completionDate: String?
    ) {
        viewModelScope.launch {
            try {
                taskEditResponse.postValue(Resource.Loading())

                val task = TaskUpdateRequest(
                    taskId = taskId,
                    taskDescriptionShort = taskDescriptionShort,
                    taskHeader = taskHeader,
                    taskDescription = taskDescription,
                    responsiblePersonEmail = responsiblePersonEmail,
                    priority = priority,
                    status = status,
                    dueDate = dueDate,
                    completionDate = completionDate
                )

                val response = RetrofitBuilder.api.updateTaskInProject(SessionManager.fetchAuthToken()!!, task)
                handleApiResponse(response, taskEditResponse)
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "Error while requesting comments: $t")
                when (t) {
                    is IOException -> taskEditResponse.postValue(Resource.Error("Network Failure"))
                    else -> taskEditResponse.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }
}