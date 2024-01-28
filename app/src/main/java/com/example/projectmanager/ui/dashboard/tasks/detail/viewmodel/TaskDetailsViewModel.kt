package com.example.projectmanager.ui.dashboard.tasks.detail.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.data.api.RetrofitBuilder
import com.example.projectmanager.data.model.tasks.TasksResponseItem
import com.example.projectmanager.data.model.tasks.comments.TaskCommentRequest
import com.example.projectmanager.data.model.tasks.comments.TaskCommentsResponse
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.ui.util.SessionManager
import com.example.projectmanager.ui.util.handleApiResponse
import kotlinx.coroutines.launch
import java.io.IOException

class TaskDetailsViewModel() : ViewModel() {

    val LOG_TAG = "TaskDetailsViewModel"

    val taskDetailsResponse: MutableLiveData<Resource<TasksResponseItem>> = MutableLiveData()
    val commentsDetailsResponse: MutableLiveData<Resource<TaskCommentsResponse>> = MutableLiveData()
    val commentPostResponse: MutableLiveData<Resource<Unit>> = MutableLiveData()


    fun getAllTaskDetails(taskId : Int) {
        viewModelScope.launch {
            try {
                taskDetailsResponse.postValue(Resource.Loading())
                //Log.d("TasksViewModel", "getAllProjectTasks: $userToken")
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



    fun getAllComments(taskId : Int){
        viewModelScope.launch {
            try {
                commentsDetailsResponse.postValue(Resource.Loading())
                //Log.d("TasksViewModel", "getAllProjectTasks: $userToken")
                val response = RetrofitBuilder.api.getTaskAllComments(SessionManager.fetchAuthToken()!!, taskId)
                handleApiResponse(response, commentsDetailsResponse)
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "error whhile requesting comments: $t")
                when (t) {
                    is IOException -> commentsDetailsResponse.postValue(Resource.Error("Network Failure"))
                    else -> commentsDetailsResponse.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }

    fun postComment(taskId: Int, commentText: String) {
        viewModelScope.launch {
            try {
                commentPostResponse.postValue(Resource.Loading())
                val request = TaskCommentRequest(taskId, commentText)
                //Log.d("TasksViewModel", "getAllProjectTasks: $userToken")
                val response = RetrofitBuilder.api.addCommentToTask(SessionManager.fetchAuthToken()!!, request)
                handleApiResponse(response, commentPostResponse)
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "error whhile requesting comments: $t")
                when (t) {
                    is IOException -> commentPostResponse.postValue(Resource.Error("Network Failure"))
                    else -> commentPostResponse.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }

}
