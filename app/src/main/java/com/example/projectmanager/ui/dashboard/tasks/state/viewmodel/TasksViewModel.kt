package com.example.projectmanager.ui.dashboard.tasks.state.viewmodel

//import com.bumptech.glide.Glide.init
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.data.api.RetrofitBuilder
import com.example.projectmanager.data.model.tasks.TasksResponse
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.ui.util.SessionManager
import com.example.projectmanager.ui.util.handleApiResponse
import kotlinx.coroutines.launch
import java.io.IOException

class TasksViewModel : ViewModel() {

    val LOG_TAG = "TasksViewModel"

    val allProjectTasks: MutableLiveData<Resource<TasksResponse>> = MutableLiveData()

    fun getAllProjectTasks(projectId: Int, taskStatus: Int) {
        viewModelScope.launch {
            try {
                allProjectTasks.postValue(Resource.Loading())
                Log.d(LOG_TAG, "getAllProjectTasks: Requesting tasks with status $taskStatus")
                val response = RetrofitBuilder.api.getProjectAllTasks(SessionManager.fetchAuthToken()!!, projectId)
                handleApiResponse(
                    response = response,
                    liveData = allProjectTasks,
                    transform = { tasksResponse ->
                        // Cast to TasksResponse and filter, then create a new TasksResponse
                        TasksResponse().apply {
                            addAll((tasksResponse).filter { it.taskStatus == taskStatus })
                        }
                    }
                )
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "getAllProjectTasks error for task status $taskStatus: $t")
                when (t) {
                    is IOException -> allProjectTasks.postValue(Resource.Error("Network Failure"))
                    else -> allProjectTasks.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }

}