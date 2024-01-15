package com.example.projectmanager.ui.dashboard.tasks.detail.edit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.data.api.RetrofitBuilder
import com.example.projectmanager.data.model.projects.manage.user.UserDetailsProjectResponse
import com.example.projectmanager.data.model.projects.manage.user.UserProjectAddRequest
import com.example.projectmanager.data.model.projects.manage.user.UserProjectRemoveRequest
import com.example.projectmanager.data.model.tasks.TasksResponseItem
import com.example.projectmanager.data.model.tasks.manage.TaskDetailsRequest
import com.example.projectmanager.data.model.tasks.manage.TaskRemoveRequest
import com.example.projectmanager.data.model.tasks.manage.TaskUpdateRequest
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.data.util.ResponseWrapper
import com.example.projectmanager.ui.util.SessionManager
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Response
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
                handleTaskDetailsResponse(response)
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
                val task = TaskRemoveRequest(taskId)
                val response = RetrofitBuilder.api.removeTaskFromProject(SessionManager.fetchAuthToken()!!, task)
                handleTaskDeleteResponse(response)
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "error whhile requesting tasks: $t")
                when (t) {
                    is IOException -> taskDeleteResponse.postValue(Resource.Error("Network Failure"))
                    else -> taskDeleteResponse.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }

    private fun handleTaskDeleteResponse(response: Response<ResponseWrapper<Unit>>) {
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {

                taskDeleteResponse.postValue(Resource.Success(Unit))

            } else {
                taskDeleteResponse.postValue(Resource.Error("Response body is null"))
            }
        } else {
            taskDeleteResponse.postValue(response.body()?.let { Resource.Error(it.reason) })
        }
    }


    fun updateTask(
        taskId: Int, taskDescriptionShort: String?, taskHeader: String?, taskDescription: String?, responsiblePersonId: Int?, priority:
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
                    responsiblePersonId = responsiblePersonId,
                    priority = priority,
                    status = status,
                    dueDate = dueDate,
                    completionDate = completionDate
                )

                val response = RetrofitBuilder.api.updateTaskInProject(SessionManager.fetchAuthToken()!!, task)
                handleTaskUpdateRequest(response)
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "error whhile requesting comments: $t")
                when (t) {
                    is IOException -> taskEditResponse.postValue(Resource.Error("Network Failure"))
                    else -> taskEditResponse.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }


    // RESPONSE HANDLERS

    private fun handleTaskUpdateRequest(response: Response<ResponseWrapper<Unit>>) {
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {

                taskEditResponse.postValue(Resource.Success(Unit))

            } else {
                taskEditResponse.postValue(Resource.Error("Response body is null"))
            }
        } else {
            taskEditResponse.postValue(response.body()?.let { Resource.Error(it.reason) })
        }
    }

    private fun handleTaskDetailsResponse(response: Response<ResponseWrapper<TasksResponseItem>>) {
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                if (responseBody.body != null) {
                    taskDetailsResponse.postValue(Resource.Success(responseBody.body))
                } else {
                    taskDetailsResponse.postValue(Resource.Error(responseBody.reason))
                }
            } else {
                taskDetailsResponse.postValue(Resource.Error("Response body is null"))
            }
        } else {
            taskDetailsResponse.postValue(Resource.Error(response.message()))
        }
    }


}