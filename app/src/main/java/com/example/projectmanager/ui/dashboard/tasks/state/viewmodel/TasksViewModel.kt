package com.example.projectmanager.ui.dashboard.tasks.state.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.bumptech.glide.Glide.init
import com.example.projectmanager.data.api.RetrofitBuilder
import com.example.projectmanager.data.util.ResponseWrapper
import com.example.projectmanager.data.model.tasks.TasksResponse
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.ui.util.SessionManager
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class TasksViewModel(private val projectId : Int, private val taskStatus : Int) : ViewModel() {

    val allProjectTasks: MutableLiveData<Resource<TasksResponse>> = MutableLiveData()

    init {
        getAllProjectTasks()
    }

    private fun getAllProjectTasks() {
        viewModelScope.launch {
            try {
                allProjectTasks.postValue(Resource.Loading())
                //Log.d("TasksViewModel", "getAllProjectTasks: $userToken")
                val response = RetrofitBuilder.api.getProjectAllTasks(SessionManager.fetchAuthToken()!!, projectId)
                handleTasksResponse(response)
            } catch (t: Throwable) {
                Log.d("TasksViewModel", "getAllProjectTasks: $t")
                when (t) {
                    is IOException -> allProjectTasks.postValue(Resource.Error("Network Failure"))
                    else -> allProjectTasks.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }

    private fun handleTasksResponse(response: Response<ResponseWrapper<TasksResponse>>) {
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                if(responseBody.body != null) {
                    allProjectTasks.postValue(Resource.Success(responseBody.body))
                } else {
                    allProjectTasks.postValue(Resource.Error(responseBody.reason))
                }
            } else {
                allProjectTasks.postValue(Resource.Error("Response body is null"))
            }
        } else {
            allProjectTasks.postValue(Resource.Error(response.message()))
        }
    }

//    fun addNewTask(description: String, priority: String) {
//        // Logika dodawania nowego zadania
//        // Możesz tutaj wykonać wywołanie API lub aktualizować listę zadań
//
//        // Przykład aktualizacji listy:
//        val newTask = ProjectTasksResponseItem(
//            // uzupełnij parametry nowego zadania
//        )
//        val updatedList = listOf(newTask) + allProjectTasks.value?.data.orEmpty()
//        allProjectTasks.postValue(Resource.Success(ProjectTasksResponse().apply { addAll(updatedList) }))
//    }
}