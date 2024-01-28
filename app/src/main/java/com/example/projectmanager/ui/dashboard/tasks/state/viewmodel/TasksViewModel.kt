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
import com.example.projectmanager.ui.util.handleApiResponse
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
                handleApiResponse(response, allProjectTasks)
            } catch (t: Throwable) {
                Log.d("TasksViewModel", "getAllProjectTasks: $t")
                when (t) {
                    is IOException -> allProjectTasks.postValue(Resource.Error("Network Failure"))
                    else -> allProjectTasks.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }

}